package ru.yandex.practicum.commerce.delivery.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.commerce.interaction.delivery.dto.DeliveryDto;

@UtilityClass
public class DeliveryMapper {
    public static Delivery toModel(DeliveryDto dto) {
        return Delivery.builder()
                .orderId(dto.getOrderId())
                .fromAddress(AddressMapper.toModel(dto.getFromAddress()))
                .toAddress(AddressMapper.toModel(dto.getToAddress()))
                .build();
    }

    public DeliveryDto toDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .fromAddress(AddressMapper.toDto(delivery.getFromAddress()))
                .toAddress(AddressMapper.toDto(delivery.getToAddress()))
                .orderId(delivery.getOrderId())
                .deliveryState(delivery.getDeliveryState())
                .build();
    }
}
