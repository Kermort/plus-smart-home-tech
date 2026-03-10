package ru.yandex.practicum.commerce.cart.service;

import ru.yandex.practicum.commerce.interaction.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {
    ShoppingCartDto getActualCart(String username);

    ShoppingCartDto putProductIntoCart(String username, Map<UUID, Integer> products);

    void deactivateCart(String username);

    ShoppingCartDto removeProductsFromCart(String username, List<UUID> products);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);
}
