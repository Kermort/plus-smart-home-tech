package ru.yandex.practicum.service.handlers.hub;

import ru.yandex.practicum.enums.HubEventType;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventHandler {
    HubEventType getMessageType();
    void handle(HubEventProto event);
}
