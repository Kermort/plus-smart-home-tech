package ru.yandex.practicum.commerce.interaction.store;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface StoreFeignClient extends StoreOperations {
}
