package ru.yandex.practicum.mapper.sensor;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import java.time.Instant;

@UtilityClass
public class SwitchSensorEventMapper {
    public static SensorEventAvro toAvro(SensorEventProto event) {
        SwitchSensorProto switchEvent = event.getSwitchSensorEvent();

        SwitchSensorAvro payload = SwitchSensorAvro.newBuilder()
                .setState(switchEvent.getState())
                .build();

        Instant timestamp = Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos());

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();
    }
}
