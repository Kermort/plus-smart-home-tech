package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.commerce.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.delivery.enums.DeliveryState;
import ru.yandex.practicum.commerce.interaction.delivery.exception.NoDeliveryFoundException;
import ru.yandex.practicum.commerce.interaction.order.OrderFeignClient;
import ru.yandex.practicum.commerce.interaction.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction.warehouse.WarehouseFeignClient;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.ShippedToDeliveryRequest;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    @Value("${delivery.coefficients.baseCost}")
    private BigDecimal BASE_COST;
    @Value("${delivery.coefficients.address_1}")
    private BigDecimal ADDRESS_1_COEFFICIENT;
    @Value("${delivery.coefficients.address_2}")
    private BigDecimal ADDRESS_2_COEFFICIENT;
    @Value("${delivery.coefficients.fragile}")
    private BigDecimal FRAGILE_COEFFICIENT;
    @Value("${delivery.coefficients.weight}")
    private BigDecimal WEIGHT_COEFFICIENT;
    @Value("${delivery.coefficients.volume}")
    private BigDecimal VOLUME_COEFFICIENT;
    @Value("${delivery.coefficients.distance}")
    private BigDecimal DISTANCE_COEFFICIENT;

    private final DeliveryRepository deliveryRepository;
    private final WarehouseFeignClient warehouseFeignClient;
    private final OrderFeignClient orderFeignClient;

    @Override
    public DeliveryDto createNewDelivery(DeliveryDto deliveryDto) {
        log.debug("[Delivery service] create new delivery {} ", deliveryDto);
        Delivery delivery = DeliveryMapper.toModel(deliveryDto);
        delivery.setDeliveryId(UUID.randomUUID());
        delivery.setDeliveryState(DeliveryState.CREATED);
        Delivery savedDelivery = deliveryRepository.save(delivery);

        return DeliveryMapper.toDto(savedDelivery);
    }

    @Override
    public void successful(UUID deliveryId) {
        log.debug("[Delivery service] successful delivery {} ", deliveryId);
        Delivery delivery = getDeliveryIfExists(deliveryId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
        orderFeignClient.deliverySuccess(delivery.getOrderId());
    }

    @Transactional
    @Override
    public void picked(UUID deliveryId) {
        log.debug("[Delivery service] delivery picked {} ", deliveryId);
        Delivery delivery = getDeliveryIfExists(deliveryId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);
        ShippedToDeliveryRequest shippedToDeliveryRequest = ShippedToDeliveryRequest.builder()
                .orderId(delivery.getOrderId())
                .deliveryId(delivery.getDeliveryId())
                .build();
        warehouseFeignClient.shipped(shippedToDeliveryRequest);
        orderFeignClient.deliveryPicked(delivery.getOrderId());
    }

    @Override
    public void failed(UUID deliveryId) {
        log.debug("[Delivery service] delivery failed {} ", deliveryId);
        Delivery delivery = getDeliveryIfExists(deliveryId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
        orderFeignClient.deliveryFailed(delivery.getOrderId());
    }

    @Override
    public BigDecimal calculateCost(OrderDto orderDto) {
        log.debug("[Delivery service] calculate cost for order {} ", orderDto.getOrderId());
        Delivery delivery = getDeliveryIfExists(orderDto.getDeliveryId());

        BigDecimal result = BigDecimal.ZERO;
        if (delivery.getFromAddress().getCountry().equals("ADDRESS_1")) {
            result = BASE_COST.add(BASE_COST.multiply(ADDRESS_1_COEFFICIENT));
        } else if (delivery.getFromAddress().getCountry().equals("ADDRESS_2")) {
            result = BASE_COST.add(BASE_COST.multiply(ADDRESS_2_COEFFICIENT));
        }

        if (orderDto.getFragile()) {
            result = result.add(result.multiply(FRAGILE_COEFFICIENT));
        }

        result = result.add(orderDto.getDeliveryWeight().multiply(WEIGHT_COEFFICIENT));

        result = result.add(orderDto.getDeliveryVolume().multiply(VOLUME_COEFFICIENT));

        if (!delivery.getFromAddress().getStreet().equals(delivery.getToAddress().getStreet())) {
            result = result.add(result.multiply(DISTANCE_COEFFICIENT));
        }

        return result;
    }

    private Delivery getDeliveryIfExists(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException(HttpStatus.NOT_FOUND, "delivery " + deliveryId + " not found"));
    }

}
