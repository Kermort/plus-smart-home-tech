package ru.yandex.practicum.commerce.order.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction.order.OrderOperations;
import ru.yandex.practicum.commerce.interaction.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction.order.dto.ProductReturnRequest;
import ru.yandex.practicum.commerce.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController implements OrderOperations {
    private final OrderService orderService;

    @Override
    public ResponseEntity<List<OrderDto>> getOrders(@RequestParam @NotNull String username) {
        log.debug("[Order controller] get orders for username {}", username);
        List<OrderDto> result = orderService.getOrders(username);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> createNewOrder(@RequestBody @Valid CreateNewOrderRequest request) {
        log.debug("[Order controller] create new order request {} ", request);
        OrderDto result = orderService.createNewOrder(request);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> returnOrder(@RequestBody @Valid ProductReturnRequest request) {
        log.debug("[Order controller] return order request {} ", request);
        OrderDto result = orderService.returnOrder(request);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> paymentSuccess(@RequestBody @NotNull UUID orderId) {
        log.debug("[Order controller] payment for order {} ", orderId);
        OrderDto result = orderService.paymentSuccess(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> paymentFailed(@RequestBody @NotNull UUID orderId) {
        log.debug("[Order controller] payment failed for order {} ", orderId);
        OrderDto result = orderService.paymentFailed(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> deliverySuccess(@RequestBody @NotNull UUID orderId) {
        log.debug("[Order controller] delivery for order {} ", orderId);
        OrderDto result = orderService.deliverySuccess(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> deliveryFailed(@RequestBody @NotNull UUID orderId) {
        log.debug("[Order controller] delivery failed for order {} ", orderId);
        OrderDto result = orderService.deliveryFailed(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> deliveryPicked(@RequestBody @NotNull UUID orderId) {
        log.debug("[Order controller] delivery picked for order {} ", orderId);
        OrderDto result = orderService.deliveryPicked(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> completed(@RequestBody @NotNull UUID orderId) {
        log.debug("[Order controller] completed order {} ", orderId);
        OrderDto result = orderService.completed(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> calculateTotal(@RequestBody @NotNull UUID orderId) {
        log.debug("[Order controller] calculate total price for order {} ", orderId);
        OrderDto result = orderService.calculateTotal(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> calculateDelivery(@RequestBody @NotNull UUID orderId) {
        log.debug("[Order controller] calculate delivery price for order {} ", orderId);
        OrderDto result = orderService.calculateDelivery(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> assembly(@RequestBody @NotNull UUID orderId) {
        log.debug("[Order controller] assembly order {} ", orderId);
        OrderDto result = orderService.assembly(orderId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<OrderDto> assemblyFailed(@RequestBody @NotNull UUID orderId) {
        log.debug("[Order controller] assembly order failed {} ", orderId);
        OrderDto result = orderService.assemblyFailed(orderId);
        return ResponseEntity.ok(result);
    }
}
