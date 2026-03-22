package ru.yandex.practicum.commerce.store.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.general.exception.ValidationException;
import ru.yandex.practicum.commerce.interaction.store.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.store.service.StoreService;
import ru.yandex.practicum.commerce.interaction.store.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.store.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.store.enums.ProductCategory;
import ru.yandex.practicum.commerce.interaction.store.enums.QuantityState;
import ru.yandex.practicum.commerce.interaction.store.StoreOperations;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class StoreController implements StoreOperations {
    private final StoreService storeService;

    @Override
    public ResponseEntity<Page<ProductDto>> findProductsByCategory(@RequestParam ProductCategory category, Pageable pageable) {
        log.debug("[Shopping store controller] GET find products by category {} ", category);
        Page<ProductDto> result = storeService.getProductsByCategory(category, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<ProductDto> addProduct(@Valid @NotNull @RequestBody ProductDto productDto) {
        log.debug("[Shopping store controller] PUT {} add product ", productDto);
        ProductDto result = storeService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<ProductDto> updateProduct(@Valid @NotNull @RequestBody ProductDto productDto) {
        log.debug("[Shopping store controller] POST update product {}", productDto);
        ProductDto result = storeService.updateProduct(productDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Boolean> removeProduct(@Valid @NotNull @RequestBody UUID uuid) {
        log.debug("[Shopping store controller] POST remove by ID {} ", uuid);
        Boolean result = storeService.removeProduct(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Boolean> setQuantityState(@Valid @RequestBody(required = false) SetProductQuantityStateRequest fromBody,
                                    @RequestParam(required = false) UUID productId,
                                    @RequestParam(required = false) QuantityState quantityState) {
        log.debug("[Shopping store controller] POST set state request");
        Boolean result = storeService.setProductQuantityState(productId, quantityState, fromBody);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(@NotNull @PathVariable UUID productId) {
        log.debug("[Shopping store controller] GET find by ID {} ", productId);
        ProductDto result = storeService.findProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
