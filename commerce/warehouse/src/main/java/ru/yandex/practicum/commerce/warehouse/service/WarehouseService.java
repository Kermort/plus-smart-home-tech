package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void newProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkProductsFromCart(ShoppingCartDto cartDto);

    void addProduct(AddProductToWarehouseRequest request);

    AddressDto getAddress();

    BookedProductsDto assembly(AssemblyProductsForOrderRequest request);

    void shippedToDelivery(ShippedToDeliveryRequest request);

    void returnProducts(Map<UUID, Integer> request);
}
