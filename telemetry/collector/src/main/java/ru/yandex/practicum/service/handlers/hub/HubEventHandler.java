package ru.yandex.practicum.service.handlers.hub;

import ru.yandex.practicum.enums.HubEventType;
import ru.yandex.practicum.model.hub.HubEvent;

public interface HubEventHandler {
    HubEventType getMessageType();
    void handle(HubEvent event);
}
