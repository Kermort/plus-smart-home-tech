package ru.yandex.practicum.analyzer.service.handlers.hub;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {
    String getType();

    void handle (HubEventAvro event);
}
