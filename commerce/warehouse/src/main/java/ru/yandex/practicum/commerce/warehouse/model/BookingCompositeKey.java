package ru.yandex.practicum.commerce.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCompositeKey {
    private UUID productId;
    private UUID orderId;
}
