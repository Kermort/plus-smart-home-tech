package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;
import ru.yandex.practicum.commerce.interaction.warehouse.WarehouseOperations;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseOperations {
    private final WarehouseService warehouseService;

    @Override
    public ResponseEntity<String> newProduct(@Valid @NotNull @RequestBody NewProductInWarehouseRequest request)
            throws SpecifiedProductAlreadyInWarehouseException {
        log.debug("[Warehouse controller] new product {} ", request);
        warehouseService.newProduct(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<BookedProductsDto> checkProductsInCart(@Valid @NotNull @RequestBody ShoppingCartDto cartDto)
            throws ProductInShoppingCartLowQuantityInWarehouse {
        log.debug("[Warehouse controller] check products from cart in warehouse {} ", cartDto);
        BookedProductsDto result = warehouseService.checkProductsFromCart(cartDto);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<String> addProductToWarehouse(@Valid @NotNull @RequestBody AddProductToWarehouseRequest request)
            throws NoSpecifiedProductInWarehouseException {
        log.debug("[Warehouse controller] add product to warehouse request {} ", request);
        warehouseService.addProduct(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<AddressDto> getAddress() {
        log.debug("[Warehouse controller] get address request");
        AddressDto result = warehouseService.getAddress();
        return ResponseEntity.ok(result);
    }
}
