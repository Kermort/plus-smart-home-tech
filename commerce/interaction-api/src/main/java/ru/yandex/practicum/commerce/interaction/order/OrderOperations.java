package ru.yandex.practicum.commerce.interaction.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction.order.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderOperations {
    @GetMapping
    ResponseEntity<List<OrderDto>> getOrders(@RequestParam @NotNull String username);

    @PutMapping
    ResponseEntity<OrderDto> createNewOrder(@RequestBody @Valid CreateNewOrderRequest request);

    @PostMapping("/return")
    ResponseEntity<OrderDto> returnOrder(@RequestBody @Valid ProductReturnRequest request);

    @PostMapping("/payment")
    ResponseEntity<OrderDto> paymentSuccess(@RequestBody @NotNull UUID orderId);

    @PostMapping("/payment/failed")
    ResponseEntity<OrderDto> paymentFailed(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery")
    ResponseEntity<OrderDto> deliverySuccess(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery/failed")
    ResponseEntity<OrderDto> deliveryFailed(@RequestBody @NotNull UUID orderId);

    @PostMapping("delivery/picked")
    ResponseEntity<OrderDto> deliveryPicked(@RequestBody @NotNull UUID orderId);

    @PostMapping("/completed")
    ResponseEntity<OrderDto> completed(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/total")
    ResponseEntity<OrderDto> calculateTotal(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/delivery")
    ResponseEntity<OrderDto> calculateDelivery(@RequestBody @NotNull UUID orderId);

    @PostMapping("/assembly")
    ResponseEntity<OrderDto> assembly(@RequestBody @NotNull UUID orderId);

    @PostMapping("/assembly/failed")
    ResponseEntity<OrderDto> assemblyFailed(@RequestBody @NotNull UUID orderId);
}
