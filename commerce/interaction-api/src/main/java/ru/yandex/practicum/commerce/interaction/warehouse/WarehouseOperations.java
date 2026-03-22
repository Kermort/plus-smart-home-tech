package ru.yandex.practicum.commerce.interaction.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseOperations {
    @PutMapping
    ResponseEntity<Void> newProduct(@Valid @NotNull @RequestBody NewProductInWarehouseRequest request);

    @PostMapping("/check")
    ResponseEntity<BookedProductsDto> checkProductsInCart(@Valid @NotNull @RequestBody ShoppingCartDto cartDto);

    @PostMapping("/add")
    ResponseEntity<Void> addProductToWarehouse(@Valid @NotNull @RequestBody AddProductToWarehouseRequest request);

    @GetMapping("/address")
    ResponseEntity<AddressDto> getAddress();

    @PostMapping("/assembly")
    ResponseEntity<BookedProductsDto> assembly(@RequestBody @NotNull @Valid AssemblyProductsForOrderRequest request);

    @PostMapping("/shipped")
    ResponseEntity<Void> shipped(@RequestBody @NotNull @Valid ShippedToDeliveryRequest request);

    @PostMapping("/return")
    ResponseEntity<Void> returnProducts(@RequestBody @NotNull @NotEmpty Map<UUID, Integer> request);
}
