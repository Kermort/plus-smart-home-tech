package ru.yandex.practicum.service.handlers.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.enums.HubEventType;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.mapper.hub.ScenarioAddedEventMapper;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.hub.scenario.ScenarioAddedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {
    @Value("${collector.kafka.producer.topics.hubs-events}")
    private String hubEventTopic;
    private final KafkaClient kafkaClient;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEvent event) {
        kafkaClient.send(hubEventTopic,
                         event.getHubId(),
                         ScenarioAddedEventMapper.toAvro((ScenarioAddedEvent) event));
    }
}
