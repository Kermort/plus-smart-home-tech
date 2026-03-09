package ru.yandex.practicum.commerce.cart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductCompositeKey {
    private UUID cartId;
    private UUID productId;
}
