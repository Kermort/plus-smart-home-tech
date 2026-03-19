package ru.yandex.practicum.commerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.order.model.OrderProduct;
import ru.yandex.practicum.commerce.order.model.OrderProductCompositeKey;

import java.util.List;
import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductCompositeKey> {
    List<OrderProduct> findAllByOrderIdIn(List<UUID> orderIds);

    List<OrderProduct> findAllByOrderId(UUID orderId);
}
