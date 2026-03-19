package ru.yandex.practicum.commerce.cart.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cart_product", schema = "carts")
@Getter
@Setter
@EqualsAndHashCode(of = {"cartId", "productId"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(CartProductCompositeKey.class)
public class CartProduct {
    @Id
    @Column(name = "cart_id")
    private UUID cartId;

    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "quantity")
    private Integer quantity;
}
