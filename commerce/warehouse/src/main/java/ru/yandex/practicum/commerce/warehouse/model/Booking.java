package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(schema = "warehouse", name = "booking")
@EqualsAndHashCode(of = {"productId", "orderId"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(BookingCompositeKey.class)
public class Booking {
    @Id
    @Column
    private UUID productId;

    @Id
    @Column
    private UUID orderId;

    @Column
    private UUID deliveryId;

    @Column
    private Integer quantity;
}
