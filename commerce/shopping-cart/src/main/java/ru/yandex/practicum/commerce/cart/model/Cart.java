package ru.yandex.practicum.commerce.cart.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.interaction.cart.enums.CartState;

import java.util.UUID;

@Entity
@Table(name = "cart", schema = "carts")
@Getter
@Setter
@EqualsAndHashCode(of = "cartId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @Column(name = "cart_id", nullable = false)
    private UUID cartId;

    @Column(name = "username")
    private String username;

    @Column(name = "cart_state")
    @Enumerated(EnumType.STRING)
    private CartState cartState;


}
