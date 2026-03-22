package ru.yandex.practicum.commerce.interaction.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.commerce.interaction.order.enums.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    @NotNull
    private UUID orderId;

    @NotNull
    private String username;

    private UUID shoppingCartId;

    @NotNull
    private Map<UUID, Integer> products;

    private UUID paymentId;

    private UUID deliveryId;

    private OrderState state;

    private BigDecimal deliveryWeight;

    private BigDecimal deliveryVolume;

    private Boolean fragile;

    private BigDecimal totalPrice;

    private BigDecimal deliveryPrice;

    private BigDecimal productPrice;
}
