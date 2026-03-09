package ru.yandex.practicum.commerce.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.interaction.store.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.store.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.store.enums.ProductCategory;
import ru.yandex.practicum.commerce.interaction.store.enums.QuantityState;

import java.util.UUID;

public interface StoreService {
    Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean removeProduct(UUID uuid);

    Boolean setProductQuantityState(UUID uuid, QuantityState quantityState, SetProductQuantityStateRequest fromBody);

    ProductDto findProductById(UUID uuid);
}
