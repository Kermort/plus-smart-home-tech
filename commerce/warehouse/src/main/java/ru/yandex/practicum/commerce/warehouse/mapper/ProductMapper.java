package ru.yandex.practicum.commerce.warehouse.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.warehouse.model.Dimension;
import ru.yandex.practicum.commerce.warehouse.model.Product;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.NewProductInWarehouseRequest;

@UtilityClass
public class ProductMapper {
    public static Product toProduct(NewProductInWarehouseRequest request) {
        Dimension dimension = Dimension.builder()
                .depth(request.getDimension().getDepth())
                .width(request.getDimension().getWidth())
                .height(request.getDimension().getHeight())
                .build();

        return Product.builder()
                .fragile(request.getFragile())
                .dimension(dimension)
                .weight(request.getWeight())
                .build();
    }


}
