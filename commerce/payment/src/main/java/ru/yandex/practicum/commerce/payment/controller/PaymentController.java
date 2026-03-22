package ru.yandex.practicum.commerce.payment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction.payment.PaymentOperations;
import ru.yandex.practicum.commerce.interaction.payment.dto.PaymentDto;
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController implements PaymentOperations {
    private final PaymentService paymentService;

    @Override
    public ResponseEntity<PaymentDto> createPayment(@RequestBody @NotNull @Valid OrderDto orderDto) {
        log.debug("[Payment controller] create payment for order {} ", orderDto.getOrderId());
        PaymentDto result = paymentService.createPayment(orderDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<BigDecimal> calculateTotalCost(@RequestBody @NotNull @Valid OrderDto orderDto) {
        log.debug("[Payment controller] calculate total cost for order {} ", orderDto.getOrderId());
        BigDecimal result = paymentService.calculateTotalCost(orderDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Void> success(@RequestBody @NotNull UUID paymentId) {
        log.debug("[Payment controller] refund for payment {} ", paymentId);
        paymentService.success(paymentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<BigDecimal> calculateProductsCost(@RequestBody @NotNull @Valid OrderDto orderDto) {
        log.debug("[Payment controller] calculate products cost for order {} ", orderDto.getOrderId());
        BigDecimal result = paymentService.calculateProductsCost(orderDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Void> failed(@RequestBody @NotNull UUID paymentId) {
        log.debug("[Payment controller] failed payment {} ", paymentId);
        paymentService.failed(paymentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
