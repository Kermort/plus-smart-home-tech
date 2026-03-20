package ru.yandex.practicum.commerce.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.cart.service.CartService;
import ru.yandex.practicum.commerce.interaction.cart.CartOperations;
import ru.yandex.practicum.commerce.interaction.cart.exception.NoProductInShoppingCartException;
import ru.yandex.practicum.commerce.interaction.cart.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.interaction.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.general.exception.ValidationException;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class CartController implements CartOperations {
    private final CartService cartService;

    @Override
    public ResponseEntity<ShoppingCartDto> getActualCart(@RequestParam String username) {
        log.debug("[Cart controller] GET actual cart by username {} ", username);
        ShoppingCartDto result = cartService.getActualCart(username);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<ShoppingCartDto> putProductIntoCart(
            @RequestParam String username,
            @Valid @NotEmpty @NotNull @RequestBody Map<UUID, @Positive Integer> products) {
        log.debug("[Cart controller] PUT product into the cart (username {}) ", username);
        ShoppingCartDto result = cartService.putProductIntoCart(username, products);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<String> deactivateCart(@RequestParam String username) {
        log.debug("[Cart controller] deactivate cart for username {} ", username);
        cartService.deactivateCart(username);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @Override
    public ResponseEntity<ShoppingCartDto> removeProductsFromCart(
            @RequestParam String username,
            @Valid @NotNull @NotEmpty @RequestBody List<UUID> products) {
        log.debug("[Cart controller] remove products ({}) from cart for username {} ", products, username);
        ShoppingCartDto result = cartService.removeProductsFromCart(username, products);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<ShoppingCartDto> changeProductsQuantity(
            @RequestParam String username,
            @Valid @NotNull @RequestBody ChangeProductQuantityRequest request) {
        log.debug("[Cart controller] change product quantity ({}) ", request);
        ShoppingCartDto result = cartService.changeProductQuantity(username, request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
