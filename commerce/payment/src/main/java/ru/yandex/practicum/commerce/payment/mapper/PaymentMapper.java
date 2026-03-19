package ru.yandex.practicum.commerce.payment.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.interaction.payment.dto.PaymentDto;
import ru.yandex.practicum.commerce.payment.model.Payment;

@UtilityClass
public class PaymentMapper {
    public static PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .totalPayment(payment.getTotalPrice())
                .deliveryTotal(payment.getDeliveryTotal())
                .feeTotal(payment.getFeeTotal())
                .build();
    }
}
