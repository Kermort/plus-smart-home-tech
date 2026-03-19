package ru.yandex.practicum.commerce.payment.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.interaction.payment.enums.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payment", schema = "payments")
@Getter
@Setter
@EqualsAndHashCode(of = "paymentId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "products_total", precision = 19, scale = 2)
    private BigDecimal productsTotal;

    @Column(name = "fee_total", precision = 19, scale = 2)
    private BigDecimal feeTotal;

    @Column(name = "delivery_total", precision = 19, scale = 2)
    private BigDecimal deliveryTotal;

    @Column(name = "total_price", precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "payment_state")
    @Enumerated(EnumType.STRING)
    private PaymentState state;

}
