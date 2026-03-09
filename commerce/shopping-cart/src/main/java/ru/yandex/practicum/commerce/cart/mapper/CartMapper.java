package ru.yandex.practicum.commerce.cart.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.cart.model.Cart;
import ru.yandex.practicum.commerce.cart.model.CartProduct;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CartMapper {
    public static ShoppingCartDto toDto(Cart cart, List<CartProduct> cartProductList) {
        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getCartId())
                .products(cartProductList.stream().collect(Collectors.toMap(CartProduct::getProductId, CartProduct::getQuantity)))
                .build();
    }
}
