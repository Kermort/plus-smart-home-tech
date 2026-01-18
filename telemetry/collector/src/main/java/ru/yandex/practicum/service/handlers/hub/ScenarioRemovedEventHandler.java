package ru.yandex.practicum.service.handlers.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.enums.HubEventType;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.mapper.hub.ScenarioRemovedEventMapper;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.hub.scenario.ScenarioRemovedEvent;

@Component
@RequiredArgsConstructor
public class ScenarioRemovedEventHandler implements HubEventHandler {
    @Value("${collector.kafka.producer.topics.hubs-events}")
    private String hubEventTopic;
    private final KafkaClient kafkaClient;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEvent event) {
        kafkaClient.send(hubEventTopic,
                event.getHubId(),
                ScenarioRemovedEventMapper.toAvro((ScenarioRemovedEvent) event));
    }
}
