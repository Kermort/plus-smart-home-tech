package ru.yandex.practicum.mapper.sensor;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

import java.time.Instant;

@UtilityClass
public class ClimateSensorEventMapper {
    public static SensorEventAvro toAvro(SensorEventProto event) {
        ClimateSensorProto climateEvent = event.getClimateSensorEvent();

        ClimateSensorAvro payload = ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateEvent.getTemperatureC())
                .setCo2Level(climateEvent.getCo2Level())
                .setHumidity(climateEvent.getHumidity())
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
