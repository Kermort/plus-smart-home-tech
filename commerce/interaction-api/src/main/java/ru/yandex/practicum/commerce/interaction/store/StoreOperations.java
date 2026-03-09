package ru.yandex.practicum.commerce.interaction.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.general.exception.ValidationException;
import ru.yandex.practicum.commerce.interaction.store.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.store.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.store.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.interaction.store.enums.QuantityState;
import ru.yandex.practicum.commerce.interaction.store.exception.ProductNotFoundException;

import java.util.UUID;

public interface StoreOperations {
    @GetMapping
    ResponseEntity<Page<ProductDto>> findProductsByCategory(@RequestParam ProductCategory category, Pageable pageable);

    @PutMapping
    ResponseEntity<ProductDto> addProduct(@Valid @NotNull @RequestBody ProductDto productDto)
            throws ValidationException;

    @PostMapping
    ResponseEntity<ProductDto> updateProduct(@Valid @NotNull @RequestBody ProductDto productDto)
            throws ValidationException, ProductNotFoundException;

    @PostMapping("/removeProductFromStore")
    ResponseEntity<Boolean> removeProduct(@Valid @NotNull @RequestBody UUID uuid)
            throws ProductNotFoundException;

    @PostMapping("/quantityState")
    ResponseEntity<Boolean> setQuantityState(@Valid @RequestBody(required = false) SetProductQuantityStateRequest fromBody,
                                             @RequestParam(required = false) UUID productId,
                                             @RequestParam(required = false) QuantityState quantityState)
            throws ValidationException, ProductNotFoundException;

    @GetMapping("/{productId}")
    ResponseEntity<ProductDto> getProductById(@NotNull @PathVariable UUID productId)
            throws ProductNotFoundException;

}
