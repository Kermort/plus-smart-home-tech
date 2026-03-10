package ru.yandex.practicum.commerce.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.cart.model.CartProduct;
import ru.yandex.practicum.commerce.cart.model.CartProductCompositeKey;

import java.util.List;
import java.util.UUID;

public interface CartProductRepository extends JpaRepository<CartProduct, CartProductCompositeKey> {
    List<CartProduct> findByCartId(UUID cartId);
}
