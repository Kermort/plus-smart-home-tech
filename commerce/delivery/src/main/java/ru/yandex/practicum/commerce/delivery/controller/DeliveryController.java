package ru.yandex.practicum.commerce.delivery.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.commerce.interaction.delivery.DeliveryOperations;
import ru.yandex.practicum.commerce.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController implements DeliveryOperations {
    private final DeliveryService deliveryService;

    @Override
    public ResponseEntity<DeliveryDto> createNewDelivery(@RequestBody @NotNull @Valid DeliveryDto deliveryDto) {
        log.debug("[Delivery controller] create new delivery {} ", deliveryDto);
        DeliveryDto result = deliveryService.createNewDelivery(deliveryDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Void> successful(@RequestBody @NotNull UUID deliveryId) {
        log.debug("[Delivery controller] successful delivery {} ", deliveryId);
        deliveryService.successful(deliveryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Void> picked(@RequestBody @NotNull UUID deliveryId) {
        log.debug("[Delivery controller] delivery picked {} ", deliveryId);
        deliveryService.picked(deliveryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Void> failed(@RequestBody @NotNull UUID deliveryId) {
        log.debug("[Delivery controller] delivery failed {} ", deliveryId);
        deliveryService.failed(deliveryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<BigDecimal> calculateCost(@RequestBody @Valid OrderDto orderDto) {
        log.debug("[Delivery controller] calculate delivery cost for order {} ", orderDto.getOrderId());
        BigDecimal result = deliveryService.calculateCost(orderDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
