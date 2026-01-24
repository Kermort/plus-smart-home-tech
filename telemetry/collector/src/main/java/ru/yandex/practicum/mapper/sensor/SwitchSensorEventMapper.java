package ru.yandex.practicum.mapper.sensor;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.model.sensor.SwitchSensorEvent;

@UtilityClass
public class SwitchSensorEventMapper {
    public static SensorEventAvro toAvro(SwitchSensorEvent event) {
        SwitchSensorAvro payload = SwitchSensorAvro.newBuilder()
                .setState(event.getState())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
