package ru.yandex.practicum.mapper.hub;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.model.hub.device.DeviceAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@UtilityClass
public class DeviceAddedEventMapper {
    public static HubEventAvro toAvro(DeviceAddedEvent event) {
        DeviceAddedEventAvro payload = DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(DeviceTypeAvro.valueOf(event.getDeviceType().name()))
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
