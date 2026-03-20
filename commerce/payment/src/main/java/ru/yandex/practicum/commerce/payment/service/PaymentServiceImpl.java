package ru.yandex.practicum.commerce.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interaction.general.exception.ValidationException;
import ru.yandex.practicum.commerce.interaction.order.OrderFeignClient;
import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction.payment.dto.PaymentDto;
import ru.yandex.practicum.commerce.interaction.payment.enums.PaymentState;
import ru.yandex.practicum.commerce.interaction.payment.exception.NoPaymentFoundException;
import ru.yandex.practicum.commerce.interaction.payment.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.commerce.interaction.store.StoreFeignClient;
import ru.yandex.practicum.commerce.interaction.store.dto.ProductDto;
import ru.yandex.practicum.commerce.payment.mapper.PaymentMapper;
import ru.yandex.practicum.commerce.payment.model.Payment;
import ru.yandex.practicum.commerce.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderFeignClient orderFeignClient;
    private final StoreFeignClient storeFeignClient;

    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        log.debug("[Payment service] create payment for order {} ", orderDto.getOrderId());
        if (orderDto.getShoppingCartId() == null || orderDto.getTotalPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(HttpStatus.BAD_REQUEST, "can't create payment because some data is missing");
        }

        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .orderId(orderDto.getOrderId())
                .productsTotal(orderDto.getTotalPrice())
                .feeTotal(orderDto.getProductPrice().multiply(BigDecimal.valueOf(0.1)))
                .deliveryTotal(orderDto.getDeliveryPrice())
                .totalPrice(orderDto.getTotalPrice())
                .state(PaymentState.PENDING)
                .build();

        Payment saved = paymentRepository.save(payment);
        return PaymentMapper.toDto(saved);
    }

    @Override
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        log.debug("[Payment service] calculate total cost for order {} ", orderDto.getOrderId());
        BigDecimal deliveryCost = orderDto.getDeliveryPrice();
        BigDecimal productsCost = orderDto.getProductPrice();

        if (deliveryCost == null || productsCost == null) {
            throw new NotEnoughInfoInOrderToCalculateException(HttpStatus.BAD_REQUEST,
                    "can't calculate total cost because delivery or products price is null for order {" + orderDto.getOrderId());
        }

        BigDecimal fee = productsCost.multiply(BigDecimal.valueOf(0.1));
        return productsCost.add(fee).add(deliveryCost);
    }

    @Override
    public void success(UUID paymentId) {
        log.debug("[Payment service] successful payment {} ", paymentId);
        Payment payment = getPaymentIfExists(paymentId);

        payment.setState(PaymentState.SUCCESS);
        paymentRepository.save(payment);
        orderFeignClient.paymentSuccess(payment.getOrderId());

    }

    @Override
    public BigDecimal calculateProductsCost(OrderDto orderDto) {
        log.debug("[Payment service] calculate products cost for order {} ", orderDto.getOrderId());
        if (orderDto.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException(HttpStatus.BAD_REQUEST, "order " + orderDto.getOrderId() + " has no products");
        }

        Map<UUID, ProductDto> products = new HashMap<>();
        for (UUID uuid: orderDto.getProducts().keySet()) {
            products.put(uuid, storeFeignClient.getProductById(uuid).getBody());
        }

        BigDecimal result = BigDecimal.ZERO;
        for (Map.Entry<UUID, ProductDto> entry: products.entrySet()) {
            result = result.add(entry.getValue().getPrice().multiply(BigDecimal.valueOf(orderDto.getProducts().get(entry.getKey()))));
        }

        return result;
    }

    @Override
    public void failed(UUID paymentId) {
        log.debug("[Payment service] failed payment {} ", paymentId);
        Payment payment = getPaymentIfExists(paymentId);

        if (payment.getState().equals(PaymentState.SUCCESS)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "payment " + paymentId + " already has SUCCESS status");
        }

        payment.setState(PaymentState.FAILED);
        paymentRepository.save(payment);
        orderFeignClient.paymentFailed(payment.getOrderId());

    }

    private Payment getPaymentIfExists(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException(HttpStatus.NOT_FOUND, "payment " + paymentId + " not found"));
    }
}
