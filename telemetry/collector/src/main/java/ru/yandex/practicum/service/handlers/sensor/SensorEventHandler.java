package ru.yandex.practicum.service.handlers.sensor;

import ru.yandex.practicum.enums.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventHandler {
    SensorEventType getMessageType();

    void handle(SensorEventProto event);
}
