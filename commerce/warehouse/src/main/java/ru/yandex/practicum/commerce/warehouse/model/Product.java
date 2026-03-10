package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(schema = "warehouse", name = "products")
@EqualsAndHashCode(of = "productId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "fragile")
    private Boolean fragile;

    @Embedded
    private Dimension dimension;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "quantity")
    private Integer quantity;
}
