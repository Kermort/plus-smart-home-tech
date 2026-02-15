package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.repository.SnapshotRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {
    private final SnapshotRepository snapshotRepository;

    public Optional<SensorsSnapshotAvro> handleRecord(ConsumerRecord<String, SensorEventAvro> record) {
        log.info("[Snapshot service] поступила запись на обработку: topic={}, partition={}, offset={}, hubId={}, timestamp={}",
                record.topic(), record.partition(), record.offset(), record.key(), record.timestamp());

        Optional<SensorsSnapshotAvro> result = updateSnapshot(record.value());
        result.ifPresent(snapshotRepository::save);
        return result;
    }

    private Optional<SensorsSnapshotAvro> updateSnapshot(SensorEventAvro event) {
        log.info("[Snapshot service] начало проверки снапшота hubId={}", event.getHubId());
        Optional<SensorsSnapshotAvro> snapshotOpt = snapshotRepository.findByHubId(event.getHubId());

        if (snapshotOpt.isEmpty()) {
            log.info("[Snapshot service] снэпшот для hubId={} отсутствует, необходимо создать новый", event.getHubId());
            snapshotOpt = Optional.of(createSnapshot(event));
        } else {
            log.info("[Snapshot service] снэпшот для хаба hubId={} есть, необходимо проверить актуальность", event.getHubId());
            snapshotOpt = Optional.ofNullable(updateSnapshotState(snapshotOpt.get(), event));
        }
        return snapshotOpt;
    }

    private SensorsSnapshotAvro createSnapshot(SensorEventAvro event) {
        log.info("[Snapshot service] создание нового снэпшота для hubId={}, sensorId={}, событие {}",
                event.getHubId(), event.getId(), event.getPayload());

        Map<String, SensorStateAvro> snapshotState = new HashMap<>();
        snapshotState.put(event.getId(), SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build());

        return SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setSensorsState(snapshotState)
                .build();
    }

    private SensorsSnapshotAvro updateSnapshotState(SensorsSnapshotAvro snapshot, SensorEventAvro event) {
        log.info("[Snapshot service] обработка события для устройства id={} хаба hubId={}, событие {}",
                event.getId(), event.getHubId(), event.getPayload());
        log.info("[Snapshot service] сейчас в снапшоте hubId={} содержатся данные от датчиков с идентификаторами: {}",
                event.getHubId(), snapshot.getSensorsState().keySet().stream().toList());

        SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
        if (oldState != null && oldState.getTimestamp().isAfter(event.getTimestamp())) {
            log.info("[Snapshot service] время в снапшоте позже, чем в событии. Обновление снэпшота не требуется");
            return null;
        }
        if (oldState != null && oldState.getData().equals(event.getPayload())) {
            log.info("[Snapshot service] данные в снапшоте и событии одинаковые. Обновление снэпшота не требуется");
            return null;
        }

        Map<String, SensorStateAvro> updatedStates = new HashMap<>(snapshot.getSensorsState());
        SensorStateAvro updatedState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
        updatedStates.put(event.getId(), updatedState);
        SensorsSnapshotAvro updatedSnapshot = SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setSensorsState(updatedStates)
                .build();

        if (oldState != null) {
            log.info("[Snapshot service] oldState={}, newState={}", oldState, updatedStates.get(event.getId()));
        } else {
            log.info("[Snapshot service] Событие добавлено к снапшоту");
        }
        return updatedSnapshot;
    }

}
