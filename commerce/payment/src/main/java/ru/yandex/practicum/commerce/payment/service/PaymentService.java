package ru.yandex.practicum.commerce.payment.service;

import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction.payment.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(OrderDto orderDto);

    BigDecimal calculateTotalCost(OrderDto orderDto);

    void refund(UUID paymentId);

    BigDecimal calculateProductsCost(OrderDto orderDto);

    void failed(UUID paymentId);
}
