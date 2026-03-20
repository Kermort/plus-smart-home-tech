package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.*;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;
import ru.yandex.practicum.commerce.interaction.warehouse.WarehouseOperations;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseOperations {
    private final WarehouseService warehouseService;

    @Override
    public ResponseEntity<String> newProduct(@Valid @NotNull @RequestBody NewProductInWarehouseRequest request) {
        log.debug("[Warehouse controller] new product {} ", request);
        warehouseService.newProduct(request);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @Override
    public ResponseEntity<BookedProductsDto> checkProductsInCart(@Valid @NotNull @RequestBody ShoppingCartDto cartDto) {
        log.debug("[Warehouse controller] check products from cart in warehouse {} ", cartDto);
        BookedProductsDto result = warehouseService.checkProductsFromCart(cartDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<String> addProductToWarehouse(@Valid @NotNull @RequestBody AddProductToWarehouseRequest request) {
        log.debug("[Warehouse controller] add product to warehouse request {} ", request);
        warehouseService.addProduct(request);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @Override
    public ResponseEntity<AddressDto> getAddress() {
        log.debug("[Warehouse controller] get address request");
        AddressDto result = warehouseService.getAddress();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<BookedProductsDto> assembly(@RequestBody @NotNull @Valid AssemblyProductsForOrderRequest request) {
        log.debug("[Warehouse controller] assembly products for order {} ", request.getOrderId());
        BookedProductsDto result = warehouseService.assembly(request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<String> shipped(@RequestBody @NotNull @Valid ShippedToDeliveryRequest request) {
        log.debug("[Warehouse controller] shipped to delivery request for order {} ", request.getOrderId());
        warehouseService.shippedToDelivery(request);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @Override
    public ResponseEntity<String> returnProducts(@RequestBody @NotNull @NotEmpty Map<UUID, Integer> request) {
        log.debug("[Warehouse controller] return products {} ", request);
        warehouseService.returnProducts(request);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
