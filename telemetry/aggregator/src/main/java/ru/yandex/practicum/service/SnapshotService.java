package ru.yandex.practicum.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

public interface SnapshotService {
    Optional<SensorsSnapshotAvro> handleRecord(ConsumerRecord<String, SensorEventAvro> record);
}
