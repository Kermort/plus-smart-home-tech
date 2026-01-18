package ru.yandex.practicum.mapper.sensor;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.model.sensor.MotionSensorEvent;

@UtilityClass
public class MotionSensorEventMapper {
    public static SensorEventAvro toAvro(MotionSensorEvent event) {
        MotionSensorAvro payload = MotionSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setMotion(event.getMotion())
                .setVoltage(event.getVoltage())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
