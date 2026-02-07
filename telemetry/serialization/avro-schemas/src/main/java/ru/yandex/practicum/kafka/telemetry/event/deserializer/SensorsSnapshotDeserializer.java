package ru.yandex.practicum.kafka.telemetry.event.deserializer;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public class SensorsSnapshotDeserializer extends BaseAvroDeserializer<SensorEventAvro> {
    public SensorsSnapshotDeserializer() {
        super(SensorEventAvro.getClassSchema());
    }
}