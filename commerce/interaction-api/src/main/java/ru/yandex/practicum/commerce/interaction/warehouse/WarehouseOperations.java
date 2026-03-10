package ru.yandex.practicum.commerce.interaction.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.NewProductInWarehouseRequest;

public interface WarehouseOperations {
    @PutMapping
    ResponseEntity<String> newProduct(@Valid @NotNull @RequestBody NewProductInWarehouseRequest request);

    @PostMapping("/check")
    ResponseEntity<BookedProductsDto> checkProductsInCart(@Valid @NotNull @RequestBody ShoppingCartDto cartDto);

    @PostMapping("/add")
    ResponseEntity<String> addProductToWarehouse(@Valid @NotNull @RequestBody AddProductToWarehouseRequest request);

    @GetMapping("/address")
    ResponseEntity<AddressDto> getAddress();
}
