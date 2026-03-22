package ru.yandex.practicum.commerce.delivery.service;


import ru.yandex.practicum.commerce.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto createNewDelivery(DeliveryDto deliveryDto);

    void successful(UUID deliveryId);

    void picked(UUID deliveryId);

    void failed(UUID deliveryId);

    BigDecimal calculateCost(OrderDto orderDto);
}
