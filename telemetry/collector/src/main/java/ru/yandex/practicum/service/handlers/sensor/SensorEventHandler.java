package ru.yandex.practicum.service.handlers.sensor;

import ru.yandex.practicum.enums.SensorEventType;
import ru.yandex.practicum.model.sensor.SensorEvent;

public interface SensorEventHandler {
    SensorEventType getMessageType();

    void handle(SensorEvent event);
}
