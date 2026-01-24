package ru.yandex.practicum.mapper.sensor;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@UtilityClass
public class ClimateSensorEventMapper {
    public static SensorEventAvro toAvro(ClimateSensorEvent event) {
        ClimateSensorAvro payload = ClimateSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureC())
                .setCo2Level(event.getCo2Level())
                .setHumidity(event.getHumidity())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
