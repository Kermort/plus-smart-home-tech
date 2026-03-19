package ru.yandex.practicum.commerce.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "order_product", schema = "orders")
@Getter
@Setter
@EqualsAndHashCode(of = {"orderId", "productId"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(OrderProductCompositeKey.class)
public class OrderProduct {
    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "quantity")
    private Integer quantity;

}
