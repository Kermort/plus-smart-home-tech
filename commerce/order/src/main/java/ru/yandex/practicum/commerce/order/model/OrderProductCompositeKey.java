package ru.yandex.practicum.commerce.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductCompositeKey {
    private UUID orderId;
    private UUID productId;
}
