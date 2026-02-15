package ru.yandex.practicum.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.service.handlers.snapshot.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SnapshotProcessor {
    private final KafkaConsumer<String, SensorsSnapshotAvro> analyzerKafkaSnapshotConsumer;
    private final SnapshotHandler snapshotHandler;

    @Value("${analyzer.kafka.consumer.topics.hub-snapshots}")
    private String hubSnapshotTopic;


    public void start() {
        log.info("[Snapshot processor] старт цикла опроса и обработки снапшотов");

        try {
            Runtime.getRuntime().addShutdownHook(new Thread(analyzerKafkaSnapshotConsumer::wakeup));

            analyzerKafkaSnapshotConsumer.subscribe(List.of(hubSnapshotTopic));
            log.info("[Snapshot processor] потребитель подписался на топики: {}", List.of(hubSnapshotTopic));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = analyzerKafkaSnapshotConsumer.poll(Duration.ofMillis(1000));
                if (!records.isEmpty()) {
                    for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                        snapshotHandler.handleSnapshot(record.value());
                    }
                    analyzerKafkaSnapshotConsumer.commitSync();
                }
            }

        } catch (WakeupException ignored) {

        } finally {
            try {
                analyzerKafkaSnapshotConsumer.commitSync();
            } catch (Exception e) {
                analyzerKafkaSnapshotConsumer.close();
            }
        }
    }
}
