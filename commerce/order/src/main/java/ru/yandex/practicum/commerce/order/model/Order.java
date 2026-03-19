package ru.yandex.practicum.commerce.order.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.interaction.order.enums.OrderState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order", schema = "orders")
@Getter
@Setter
@EqualsAndHashCode(of = "orderId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "username")
    private String username;

    @Column(name = "order_state")
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @Column(name = "cart_id")
    private UUID cartId;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "delivery_volume", precision = 19, scale = 2)
    private BigDecimal deliveryVolume;

    @Column(name = "delivery_weight", precision = 19, scale = 2)
    private BigDecimal deliveryWeight;

    @Column(name = "fragile")
    private Boolean fragile;

    @Column(name = "total_price", precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "products_price", precision = 19, scale = 2)
    private BigDecimal productsPrice;

    @Column(name = "delivery_price", precision = 19, scale = 2)
    private BigDecimal deliveryPrice;
}
