package ru.yandex.practicum.commerce.interaction.order;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderFeignClient extends OrderOperations {
}
