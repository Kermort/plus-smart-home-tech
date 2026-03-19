package ru.yandex.practicum.commerce.interaction.payment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction.payment.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentOperations {
    @PostMapping
    ResponseEntity<PaymentDto> createPayment(@RequestBody @NotNull @Valid OrderDto orderDto);

    @PostMapping("/totalCost")
    ResponseEntity<BigDecimal> calculateTotalCost(@RequestBody @NotNull @Valid OrderDto orderDto);

    @PostMapping("/refund")
    ResponseEntity<String> refund(@RequestBody @NotNull UUID paymentId);

    @PostMapping("/productCost")
    ResponseEntity<BigDecimal> calculateProductsCost(@RequestBody @NotNull @Valid OrderDto orderDto);

    @PostMapping("/failed")
    ResponseEntity<String> failed(@RequestBody @NotNull UUID paymentId);
}
