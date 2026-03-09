package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.mapper.ProductMapper;
import ru.yandex.practicum.commerce.warehouse.model.Product;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseRepository;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    @Override
    public void newProduct(NewProductInWarehouseRequest request) {
        log.debug("[Warehouse service] new product request {} ", request);
        Optional<Product> productOpt = warehouseRepository.findById(request.getProductId());
        if (productOpt.isPresent()) {
            throw new SpecifiedProductAlreadyInWarehouseException(HttpStatus.BAD_REQUEST,
                    "Product id " + request.getProductId() + " already in warehouse");
        }

        Product productToSave = ProductMapper.toProduct(request);
        productToSave.setProductId(request.getProductId());
        productToSave.setQuantity(0);
        log.debug("[Warehouse controller] product to save ID={} ", productToSave.getProductId());
        warehouseRepository.save(productToSave);
    }

    @Override
    public BookedProductsDto checkProductsFromCart(ShoppingCartDto cartDto) {
        log.debug("[Warehouse service] check products from cart in warehouse {} ", cartDto);
        Set<UUID> productIds = cartDto.getProducts().keySet();
        List<Product> productList = warehouseRepository.findAllById(productIds);

        Optional<Product> missingProduct = productList.stream()
                .filter(p -> p.getQuantity() < cartDto.getProducts().get(p.getProductId())).findFirst();

        if (missingProduct.isPresent()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse(HttpStatus.BAD_REQUEST, "Product low quantity");
        }

        return calculateDeliveryParameters(productList);

    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        log.debug("[Warehouse service] add product request {} ", request);
        Product product = warehouseRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(HttpStatus.BAD_REQUEST,
                        "Product ID " + request.getProductId() + " not in warehouse"));

        product.setQuantity(product.getQuantity() + request.getQuantity());
        warehouseRepository.save(product);
    }

    @Override
    public AddressDto getAddress() {
        log.debug("[Warehouse service] get address request");
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    private BookedProductsDto calculateDeliveryParameters(List<Product> products) {
        BigDecimal totalVolume = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;
        boolean fragile = false;

        for (Product product: products) {
            totalWeight = totalWeight.add(product.getWeight());
            BigDecimal productVolume = product.getDimension().getDepth()
                    .multiply(product.getDimension().getHeight())
                    .multiply(product.getDimension().getWidth())
                    .multiply(BigDecimal.valueOf(product.getQuantity()));
            totalVolume = totalVolume.add(productVolume);
            fragile = fragile || product.getFragile();
        }

        return BookedProductsDto.builder()
                .deliveryVolume(totalVolume)
                .deliveryWeight(totalWeight)
                .fragile(fragile)
                .build();
    }
}
