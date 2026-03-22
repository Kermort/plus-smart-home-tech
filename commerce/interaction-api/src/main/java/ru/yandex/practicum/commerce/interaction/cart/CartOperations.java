package ru.yandex.practicum.commerce.interaction.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartOperations {
    @GetMapping
    ResponseEntity<ShoppingCartDto> getActualCart(@RequestParam String username);

    @PutMapping
    ResponseEntity<ShoppingCartDto> putProductIntoCart(@RequestParam String username,
                                                       @RequestBody @Valid @NotNull @NotEmpty Map<UUID,
                                                       @Positive Integer> products);

    @DeleteMapping
    ResponseEntity<Void> deactivateCart(@RequestParam String username);

    @PostMapping("/remove")
    ResponseEntity<ShoppingCartDto> removeProductsFromCart(
            @RequestParam String username,
            @Valid @NotNull @NotEmpty @RequestBody List<UUID> products);

    @PostMapping("/change-quantity")
    ResponseEntity<ShoppingCartDto> changeProductsQuantity(
            @RequestParam String username,
            @Valid @NotNull @RequestBody ChangeProductQuantityRequest request);

}
