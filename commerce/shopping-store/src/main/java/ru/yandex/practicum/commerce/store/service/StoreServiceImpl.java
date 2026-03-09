package ru.yandex.practicum.commerce.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.store.mapper.ProductMapper;
import ru.yandex.practicum.commerce.store.model.Product;
import ru.yandex.practicum.commerce.store.repository.ProductRepository;
import ru.yandex.practicum.commerce.interaction.store.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.store.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.store.enums.ProductCategory;
import ru.yandex.practicum.commerce.interaction.store.enums.ProductState;
import ru.yandex.practicum.commerce.interaction.store.enums.QuantityState;
import ru.yandex.practicum.commerce.interaction.store.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.interaction.general.exception.ValidationException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final ProductRepository productRepository;

    @Override
    public Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable) {
        log.debug("[Shopping store service] get by category {}", category);
        return productRepository.findAllByProductCategory(category, pageable).map(ProductMapper::toDto);
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        log.debug("[Shopping service] create product {}", productDto);

        Product product = ProductMapper.toModel(productDto);
        product.setProductId(UUID.randomUUID());

        if (productRepository.existsById(product.getProductId())) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Product ID " + product.getProductId() + " already exists");
        }
        Product savedProduct = productRepository.save(product);

        return ProductMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        if (productDto.getProductId() == null) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Product ID is missing");
        }

        Product product = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(HttpStatus.NOT_FOUND, productDto.getProductId() + "not found"));

        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setQuantityState(productDto.getQuantityState());
        product.setProductState(productDto.getProductState());
        product.setPrice(productDto.getPrice());

        if (!productDto.getImageSrc().isBlank()) {
            product.setImageSrc(productDto.getImageSrc());
        }

        if (productDto.getProductCategory() != null) {
            product.setProductCategory(productDto.getProductCategory());
        }

        Product updatedProduct = productRepository.save(product);

        return ProductMapper.toDto(updatedProduct);
    }

    @Override
    public Boolean removeProduct(UUID uuid) {
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new ProductNotFoundException(HttpStatus.NOT_FOUND, uuid + " not found"));

        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        return true;
    }

    @Override
    public Boolean setProductQuantityState(UUID uuid, QuantityState quantityState, SetProductQuantityStateRequest fromBody) {
        SetProductQuantityStateRequest stateRequest = new SetProductQuantityStateRequest();

        if (fromBody != null) {
            if (uuid != null && quantityState != null) {
                if (!fromBody.getQuantityState().equals(quantityState) ||
                !fromBody.getProductId().equals(uuid)) {
                    throw new ValidationException(HttpStatus.BAD_REQUEST, "Запрос на изменение статуса передан в параметрах и в теле, но они не совпадают");
                }
            }
            stateRequest.setQuantityState(fromBody.getQuantityState());
            stateRequest.setProductId(fromBody.getProductId());
        } else {
            if (uuid == null || quantityState == null) {
                throw new ValidationException(HttpStatus.BAD_REQUEST, "Тело запроса на изменение статуса пустое, а в параметрах ошибка");
            }
            stateRequest.setProductId(uuid);
            stateRequest.setQuantityState(quantityState);
        }

        Product product = productRepository.findById(stateRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(HttpStatus.NOT_FOUND, stateRequest.getProductId() + " not found"));

        product.setQuantityState(stateRequest.getQuantityState());
        productRepository.save(product);
        return true;
    }

    @Override
    public ProductDto findProductById(UUID uuid) {
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new ProductNotFoundException(HttpStatus.NOT_FOUND, uuid + " not found"));

        return ProductMapper.toDto(product);
    }
}
