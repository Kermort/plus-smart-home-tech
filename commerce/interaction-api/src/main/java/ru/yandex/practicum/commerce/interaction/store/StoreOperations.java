package ru.yandex.practicum.commerce.interaction.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.store.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.store.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.store.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.interaction.store.enums.QuantityState;

import java.util.UUID;

public interface StoreOperations {
    @GetMapping
    ResponseEntity<Page<ProductDto>> findProductsByCategory(@RequestParam ProductCategory category, Pageable pageable);

    @PutMapping
    ResponseEntity<ProductDto> addProduct(@Valid @NotNull @RequestBody ProductDto productDto);

    @PostMapping
    ResponseEntity<ProductDto> updateProduct(@Valid @NotNull @RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    ResponseEntity<Boolean> removeProduct(@Valid @NotNull @RequestBody UUID uuid);

    @PostMapping("/quantityState")
    ResponseEntity<Boolean> setQuantityState(@Valid @RequestBody(required = false) SetProductQuantityStateRequest fromBody,
                                             @RequestParam(required = false) UUID productId,
                                             @RequestParam(required = false) QuantityState quantityState);

    @GetMapping("/{productId}")
    ResponseEntity<ProductDto> getProductById(@NotNull @PathVariable UUID productId);

}
