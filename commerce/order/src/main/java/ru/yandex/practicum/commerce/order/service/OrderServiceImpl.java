package ru.yandex.practicum.commerce.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.cart.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.interaction.delivery.DeliveryFeignClient;
import ru.yandex.practicum.commerce.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.general.exception.ValidationException;
import ru.yandex.practicum.commerce.interaction.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction.order.dto.ProductReturnRequest;
import ru.yandex.practicum.commerce.interaction.order.enums.OrderState;
import ru.yandex.practicum.commerce.interaction.order.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.payment.PaymentFeignClient;
import ru.yandex.practicum.commerce.interaction.payment.dto.PaymentDto;
import ru.yandex.practicum.commerce.interaction.warehouse.WarehouseFeignClient;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.commerce.order.model.Order;
import ru.yandex.practicum.commerce.order.model.OrderProduct;
import ru.yandex.practicum.commerce.order.repository.OrderProductRepository;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final WarehouseFeignClient warehouseFeignClient;
    private final PaymentFeignClient paymentFeignClient;
    private final DeliveryFeignClient deliveryFeignClient;

    @Override
    @Transactional
    public List<OrderDto> getOrders(String username) {
        log.debug("[Order service] get orders for username {} ", username);
        validateUsername(username);
        //получить список заказов
        List<Order> orders = orderRepository.findAllByUsername(username);
        //получить спиков товаров по заказам
        List<UUID> orderIds = orders.stream().map(Order::getOrderId).toList();
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderIdIn(orderIds);

        //разобрать товары по заказам
        Map<UUID, List<OrderProduct>> orderProductsMap = new HashMap<>();
        for (OrderProduct op: orderProductList) {
            orderProductsMap.putIfAbsent(op.getOrderId(), new ArrayList<>());
            orderProductsMap.get(op.getOrderId()).add(op);
        }

        //собрать в dto и вернуть
        return orders.stream()
                .map(o -> OrderMapper.toDto(o, orderProductsMap.get(o.getOrderId()))).toList();
    }

    @Transactional
    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest request) {
        log.debug("[Order service] create new order request {} ", request);
        validateUsername(request.getUsername());
        ShoppingCartDto cartDto = request.getShoppingCart();

        OrderDto orderDto = OrderDto.builder()
                .orderId(UUID.randomUUID())
                .username(request.getUsername())
                .shoppingCartId(cartDto.getShoppingCartId())
                .products(cartDto.getProducts())
                .build();

        //установить статус
        orderDto.setState(OrderState.NEW);

        setDeliveryData(orderDto, cartDto, request);

        //сохранить заказ в базе
        Order order = OrderMapper.toModel(orderDto);
        order.setOrderId(orderDto.getOrderId());
        Order savedOrder = orderRepository.save(order);
        List<OrderProduct> orderProductList = request.getShoppingCart().getProducts().entrySet().stream()
                .map(entry -> OrderProduct.builder()
                        .orderId(savedOrder.getOrderId())
                        .productId(entry.getKey())
                        .quantity(entry.getValue())
                        .build()).toList();

        List<OrderProduct> savedOrderProductList = orderProductRepository.saveAll(orderProductList);

        //проверить наличие товаров на складе, в т.ч. количество (не менее указанного в запросе)
        return OrderMapper.toDto(savedOrder, savedOrderProductList);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        log.debug("[Order service] return order request {} ", request);
        validateUsername(request.getUsername());
        Order order = getOrderIfExists(request.getOrderId());
        List<OrderProduct> productsInOrder = orderProductRepository.findAllByOrderId(request.getOrderId());

        //проверить, не пытаемся ли вернуть больше товаров, чем есть в заказе
        OrderProduct checkProductsQuantity = productsInOrder.stream()
                .filter(op -> op.getQuantity() - request.getProducts().get(op.getProductId()) < 0)
                .findFirst().orElseThrow(() -> new ValidationException(HttpStatus.BAD_REQUEST, "return order error"));

        //проверить, не пытаемся ли вернуть товары, которых нет в заказе
        if (request.getProducts().keySet().size() >
                productsInOrder.stream().map(OrderProduct::getProductId).collect(Collectors.toSet()).size()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "return order error");
        }

        //убрать товары из заказа
        List<OrderProduct> productsToRemove = productsInOrder.stream()
                .filter(op -> Objects.equals(op.getQuantity(), request.getProducts().get(op.getProductId())))
                        .toList();
        orderProductRepository.deleteAll(productsToRemove);

        //уменьшить количество товаров в заказе
        List<OrderProduct> productsToChangeQuantity = productsInOrder.stream()
                .filter(op -> op.getQuantity() - request.getProducts().get(op.getProductId()) > 0)
                        .toList();
        for (OrderProduct op: productsToChangeQuantity) {
            op.setQuantity(op.getQuantity() - request.getProducts().get(op.getProductId()));
        }
        orderProductRepository.saveAll(productsToChangeQuantity);

        //вернуть товары на склад
        warehouseFeignClient.returnProducts(request.getProducts());

        //получить оставшиеся в заказе товары и собрать dto
        List<OrderProduct> remainProducts = orderProductRepository.findAllByOrderId(request.getOrderId());
        OrderDto resultDto = OrderMapper.toDto(order, remainProducts);

        //проставить статус
        order.setOrderState(OrderState.PRODUCT_RETURNED);
        return resultDto;
    }

    @Override
    public OrderDto paymentSuccess(UUID orderId) {
        log.debug("[Order service] payment for order {} ", orderId);
        Order order = getOrderIfExists(orderId);

        order.setOrderState(OrderState.PAID);

        Order updatedOrder = orderRepository.save(order);
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(order.getOrderId()).stream().toList();

        return OrderMapper.toDto(updatedOrder, orderProductList);
    }

    @Transactional
    @Override
    public OrderDto paymentFailed(UUID orderId) {
        log.debug("[Order service] payment failed for order {} ", orderId);
        Order order = getOrderIfExists(orderId);

        order.setOrderState(OrderState.PAYMENT_FAILED);
        Order updatedOrder = orderRepository.save(order);
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(order.getOrderId()).stream().toList();

        return OrderMapper.toDto(updatedOrder, orderProductList);
    }

    @Override
    public OrderDto deliveryPicked(UUID orderId) {
        log.debug("[Order service] delivery picked for order {} ", orderId);
        Order order = getOrderIfExists(orderId);
        if (!order.getOrderState().equals(OrderState.ASSEMBLED)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "delivery picked can be only after ASSEMBLED status");
        }

        order.setOrderState(OrderState.ON_DELIVERY);
        Order updatedOrder = orderRepository.save(order);
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(order.getOrderId()).stream().toList();

        return OrderMapper.toDto(updatedOrder, orderProductList);
    }

    @Override
    public OrderDto deliverySuccess(UUID orderId) {
        log.debug("[Order service] delivery for order {} ", orderId);
        Order order = getOrderIfExists(orderId);
        if (!order.getOrderState().equals(OrderState.ON_DELIVERY) && !order.getOrderState().equals(OrderState.DELIVERY_FAILED)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "delivery success can be only after ON_DELIVERY or DELIVERY_FAILED status");
        }
        order.setOrderState(OrderState.DELIVERED);
        Order updatedOrder = orderRepository.save(order);
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(order.getOrderId()).stream().toList();

        return OrderMapper.toDto(updatedOrder, orderProductList);
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        log.debug("[Order service] delivery failed for order {} ", orderId);
        Order order = getOrderIfExists(orderId);
        order.setOrderState(OrderState.DELIVERY_FAILED);
        Order updatedOrder = orderRepository.save(order);
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(order.getOrderId()).stream().toList();

        return OrderMapper.toDto(updatedOrder, orderProductList);
    }

    @Override
    public OrderDto completed(UUID orderId) {
        log.debug("[Order service] completed order {} ", orderId);
        Order order = getOrderIfExists(orderId);
        if (!order.getOrderState().equals(OrderState.DELIVERED)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "not delivered order can not be completed");
        }
        order.setOrderState(OrderState.COMPLETED);
        Order updatedOrder = orderRepository.save(order);
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(order.getOrderId()).stream().toList();
        return OrderMapper.toDto(updatedOrder, orderProductList);
    }

    @Override
    public OrderDto calculateTotal(UUID orderId) {
        log.debug("[Order service] calculate total cost of order {} ", orderId);
        Order order = getOrderIfExists(orderId);
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(orderId);
        OrderDto orderDto = OrderMapper.toDto(order, orderProductList);
        setPaymentData(orderDto);
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setProductsPrice(orderDto.getProductPrice());
        order.setPaymentId(orderDto.getPaymentId());
        order.setOrderState(orderDto.getState());
        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.toDto(updatedOrder, orderProductList);
    }

    @Override
    public OrderDto calculateDelivery(UUID orderId) {
        log.debug("[Order service] calculate delivery cost of order {} ", orderId);
        Order order = getOrderIfExists(orderId);
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(orderId);
        OrderDto orderDto = OrderMapper.toDto(order, orderProductList);
        order.setDeliveryPrice(deliveryFeignClient.calculateCost(orderDto).getBody());
        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.toDto(updatedOrder, orderProductList);
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        log.debug("[Order service] assembly order {} ", orderId);
        Order order = getOrderIfExists(orderId);
        if (!order.getOrderState().equals(OrderState.PAID) && !order.getOrderState().equals(OrderState.ASSEMBLY_FAILED)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "order assembly failed (set delivery data)");
        }
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(orderId);
        OrderDto orderDto = OrderMapper.toDto(order, orderProductList);

        AssemblyProductsForOrderRequest request = AssemblyProductsForOrderRequest.builder()
                .orderId(orderId)
                .products(orderDto.getProducts())
                .build();
        warehouseFeignClient.assembly(request).getBody();
        order.setOrderState(OrderState.ASSEMBLED);
        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.toDto(updatedOrder, orderProductList);
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        log.debug("[Order service] assembly failed for order {} ", orderId);
        Order order = getOrderIfExists(orderId);
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(orderId);
        order.setOrderState(OrderState.ASSEMBLY_FAILED);
        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.toDto(updatedOrder, orderProductList);
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException(HttpStatus.UNAUTHORIZED, "Username is null or blank");
        }
    }

    private Order getOrderIfExists(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(HttpStatus.NOT_FOUND, "order " + orderId + " not found"));
    }

    private void setDeliveryData(OrderDto orderDto, ShoppingCartDto cartDto, CreateNewOrderRequest request) {
        BookedProductsDto deliveryParameters = warehouseFeignClient.checkProductsInCart(cartDto).getBody();
        AddressDto warehouseAddress = warehouseFeignClient.getAddress().getBody();
        AddressDto deliveryAddress = request.getDeliveryAddress();
        if (deliveryParameters == null || warehouseAddress == null) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "order create failed (set delivery data)");
        }

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .fromAddress(warehouseAddress)
                .toAddress(deliveryAddress)
                .orderId(orderDto.getOrderId())
                .build();

        //создать delivery
        DeliveryDto createdDeliveryDto = deliveryFeignClient.createNewDelivery(deliveryDto).getBody();
        if (createdDeliveryDto == null) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "order create failed (set delivery data)");
        }

        //установить delivery weight + volume + fragile
        orderDto.setDeliveryWeight(deliveryParameters.getDeliveryWeight());
        orderDto.setDeliveryVolume(deliveryParameters.getDeliveryVolume());
        orderDto.setFragile(deliveryParameters.getFragile());

        //установаить deliveryId + запросить стоимость доставки
        orderDto.setDeliveryId(createdDeliveryDto.getDeliveryId());

    }

    private void setPaymentData(OrderDto orderDto) {
        //запросить в payment стоимость товаров + paymentId
        if (!orderDto.getState().equals(OrderState.NEW) && !orderDto.getState().equals(OrderState.PAYMENT_FAILED)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "order create failed (set payment data)");
        }
        orderDto.setProductPrice(paymentFeignClient.calculateProductsCost(orderDto).getBody());
        orderDto.setTotalPrice(paymentFeignClient.calculateTotalCost(orderDto).getBody());
        PaymentDto paymentDto = paymentFeignClient.createPayment(orderDto).getBody();
        orderDto.setState(OrderState.ON_PAYMENT);

        if (paymentDto == null) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "order create failed (set payment data)");
        }

        orderDto.setPaymentId(paymentDto.getPaymentId());
    }

}
