package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorStarter {
    private final KafkaProducer<String, SensorsSnapshotAvro> kafkaProducer;
    private final KafkaConsumer<String, SensorEventAvro> kafkaConsumer;
    private final SnapshotService snapshotService;

    @Value("${aggregator.kafka.producer.topics.consumer-subscription}")
    private String sensorEventTopic;

    @Value("${aggregator.kafka.producer.topics.producer-topic}")
    private String sensorsSnapshotTopic;


    public void start() {
        log.info("[Aggregator starter] старт агрегатора");
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(kafkaConsumer::wakeup));

            kafkaConsumer.subscribe(List.of(sensorEventTopic));
            log.info("[Aggregator starter] потребитель подписался на топики: {}", List.of(sensorEventTopic));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = kafkaConsumer.poll(Duration.ofMillis(1000));
                if (!records.isEmpty()) {
                    for (ConsumerRecord<String, SensorEventAvro> record : records) {
                        Optional<SensorsSnapshotAvro> snapshotOpt = snapshotService.handleRecord(record);
                        snapshotOpt.ifPresent(this::sendSnapshotToKafka);
                    }
                    kafkaConsumer.commitSync();
                }
            }
        } catch(WakeupException ignored) {

        } finally {
            try {
                kafkaProducer.flush();
                kafkaConsumer.commitSync();
            } catch (Exception e) {
                kafkaConsumer.close();
                kafkaProducer.close();
            }
        }
    }

    private void sendSnapshotToKafka(final SensorsSnapshotAvro snapshot) {
        log.info("[Aggregator starter] Отправка снэпшота: hubId={}", snapshot.getHubId());

        ProducerRecord<String, SensorsSnapshotAvro> record =
                new ProducerRecord<>(sensorsSnapshotTopic, snapshot.getHubId(), snapshot);
        kafkaProducer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("[Aggregator starter] При отправке снэпшота произошла ошибка", exception);
            } else {
                log.info("[Aggregator starter] Снэпшот отправлен в топик {} с оффсетом {}", metadata.topic(), metadata.offset());
            }
        });
    }
}
