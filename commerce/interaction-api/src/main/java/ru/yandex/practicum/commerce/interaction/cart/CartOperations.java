package ru.yandex.practicum.commerce.interaction.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

public interface CartOperations {
    @GetMapping
    ResponseEntity<ShoppingCartDto> getActualCart(@RequestParam String username) throws NotAuthorizedUserException;

    @PutMapping
    ResponseEntity<ShoppingCartDto> putProductIntoCart(@RequestParam String username,
                                                       @RequestBody @Valid @NotNull @NotEmpty Map<UUID, @Positive Integer> products)
        throws NotAuthorizedUserException,
            ValidationException,
            ProductInShoppingCartLowQuantityInWarehouse,
            NoSpecifiedProductInWarehouseException;

    @DeleteMapping
    ResponseEntity<String> deactivateCart(@RequestParam String username) throws NotAuthorizedUserException;

    @PostMapping("/remove")
    ResponseEntity<ShoppingCartDto> removeProductsFromCart(
            @RequestParam String username,
            @Valid @NotNull @NotEmpty @RequestBody List<UUID> products)
            throws ValidationException, NoProductInShoppingCartException,
            NotAuthorizedUserException;

    @PostMapping("/change-quantity")
    ResponseEntity<ShoppingCartDto> changeProductsQuantity(
            @RequestParam String username,
            @Valid @NotNull @RequestBody ChangeProductQuantityRequest request)
        throws NotAuthorizedUserException, NoProductInShoppingCartException;

}
