package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.NewProductInWarehouseRequest;

public interface WarehouseService {
    void newProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkProductsFromCart(ShoppingCartDto cartDto);

    void addProduct(AddProductToWarehouseRequest request);

    AddressDto getAddress();
}
