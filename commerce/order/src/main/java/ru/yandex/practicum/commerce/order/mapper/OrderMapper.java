package ru.yandex.practicum.commerce.order.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AddressDto;
import ru.yandex.practicum.commerce.order.model.Order;
import ru.yandex.practicum.commerce.order.model.OrderProduct;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class OrderMapper {
    public static OrderDto toDto(Order order, List<OrderProduct> orderProductList) {
        Map<UUID, Integer> products = orderProductList.stream()
                .collect(Collectors.toMap(OrderProduct::getProductId, OrderProduct::getQuantity));

        return OrderDto.builder()
                .orderId(order.getOrderId())
                .username(order.getUsername())
                .shoppingCartId(order.getCartId())
                .products(products)
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .state(order.getOrderState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.getFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductsPrice())
                .build();
    }

    public static Order toModel(OrderDto dto) {
        return Order.builder()
                .username(dto.getUsername())
                .orderState(dto.getState())
                .cartId(dto.getShoppingCartId())
                .paymentId(dto.getPaymentId())
                .deliveryId(dto.getDeliveryId())
                .deliveryVolume(dto.getDeliveryVolume())
                .deliveryWeight(dto.getDeliveryWeight())
                .fragile(dto.getFragile())
                .totalPrice(dto.getTotalPrice())
                .productsPrice(dto.getProductPrice())
                .deliveryPrice(dto.getDeliveryPrice())
                .build();
    }
}
