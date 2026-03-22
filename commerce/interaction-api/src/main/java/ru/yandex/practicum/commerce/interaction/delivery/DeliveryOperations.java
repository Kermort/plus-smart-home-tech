package ru.yandex.practicum.commerce.interaction.delivery;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryOperations {
    @PutMapping
    ResponseEntity<DeliveryDto> createNewDelivery(@RequestBody @NotNull @Valid DeliveryDto deliveryDto);

    @PostMapping("/successful")
    ResponseEntity<Void> successful(@RequestBody @NotNull UUID deliveryId);

    @PostMapping("/picked")
    ResponseEntity<Void> picked(@RequestBody @NotNull UUID deliveryId);

    @PostMapping("/failed")
    ResponseEntity<Void> failed(@RequestBody @NotNull UUID deliveryId);

    @PostMapping("/cost")
    ResponseEntity<BigDecimal> calculateCost(@RequestBody @Valid OrderDto orderDto);
}
