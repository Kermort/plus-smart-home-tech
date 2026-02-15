package ru.yandex.practicum.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.service.handlers.hub.HubEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {
    private final KafkaConsumer<String, HubEventAvro> analyzerKafkaHubEventConsumer;
    private final Map<String, HubEventHandler> hubEventHandlers;

    @Value("${analyzer.kafka.consumer.topics.hubs-events}")
    private String hubEventTopic;

    public HubEventProcessor(List<HubEventHandler> hubEventHandlers, KafkaConsumer<String, HubEventAvro> consumer) {
        this.analyzerKafkaHubEventConsumer = consumer;
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getType, Function.identity()));
    }

    @Override
    public void run() {
        log.info("[Hub event processor] Старт цикла опроса и обработки событий добавления/удаления устройств и сценариев");

        try {
            Runtime.getRuntime().addShutdownHook(new Thread(analyzerKafkaHubEventConsumer::wakeup));

            analyzerKafkaHubEventConsumer.subscribe(List.of(hubEventTopic));
            log.info("[Hub event processor] потребитель подписался на топики: {}", List.of(hubEventTopic));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = analyzerKafkaHubEventConsumer.poll(Duration.ofMillis(1000));
                if (!records.isEmpty()) {
                    for (ConsumerRecord<String, HubEventAvro> record : records) {
                        String payloadName = record.value().getPayload().getClass().getSimpleName();

                        HubEventHandler handler = hubEventHandlers.get(payloadName);
                        handler.handle(record.value());
                    }
                    analyzerKafkaHubEventConsumer.commitSync();
                }
            }

        } catch (WakeupException ignored) {

        } finally {
            try {
                analyzerKafkaHubEventConsumer.commitSync();
            } catch (Exception e) {
                analyzerKafkaHubEventConsumer.close();
            }
        }
    }
}
