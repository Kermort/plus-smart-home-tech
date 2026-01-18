package ru.yandex.practicum.service.handlers.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.enums.HubEventType;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.mapper.hub.DeviceRemovedEventMapper;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.hub.device.DeviceRemovedEvent;

@Component
@RequiredArgsConstructor
public class DeviceRemovedEventHandler implements HubEventHandler {
    @Value("${collector.kafka.producer.topics.hubs-events}")
    private String hubEventTopic;
    private final KafkaClient kafkaClient;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEvent event) {
        kafkaClient.send(hubEventTopic,
                event.getHubId(),
                DeviceRemovedEventMapper.toAvro((DeviceRemovedEvent) event));
    }
}
