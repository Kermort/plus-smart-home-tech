package ru.yandex.practicum.commerce.interaction.store.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.commerce.interaction.store.enums.ProductCategory;
import ru.yandex.practicum.commerce.interaction.store.enums.ProductState;
import ru.yandex.practicum.commerce.interaction.store.enums.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private UUID productId;

    @NotNull
    private String productName;

    @NotNull
    private String description;

    private String imageSrc;

    @NotNull
    private QuantityState quantityState;

    @NotNull
    private ProductState productState;

    private ProductCategory productCategory;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @DecimalMin(value = "1.0")
    private BigDecimal price;
}
