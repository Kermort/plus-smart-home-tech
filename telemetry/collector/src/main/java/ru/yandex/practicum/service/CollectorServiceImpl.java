package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.enums.HubEventType;
import ru.yandex.practicum.enums.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.service.handlers.hub.HubEventHandler;
import ru.yandex.practicum.service.handlers.sensor.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CollectorServiceImpl implements CollectorService {
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;

    @Autowired
    public CollectorServiceImpl(List<HubEventHandler> hubEventHandlers, List<SensorEventHandler> sensorEventHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
    }

    @Override
    public void collectHubEvent(HubEventProto hubEvent) {
        log.info("[Collector service] hub event: {}", hubEvent);
        HubEventHandler handler = hubEventHandlers.get(HubEventType.valueOf(hubEvent.getPayloadCase().name()));
        if (handler == null) {
            throw new IllegalArgumentException("Не найден обработчик события для события " + hubEvent.getPayloadCase());
        }
        handler.handle(hubEvent);
    }

    @Override
    public void collectSensorEvent(SensorEventProto sensorEvent) {
        log.info("[Collector service] sensor event: {}", sensorEvent);
        SensorEventHandler handler = sensorEventHandlers.get(SensorEventType.valueOf(sensorEvent.getPayloadCase().name()));
        if (handler == null) {
            throw new IllegalArgumentException("Не найден обработчик события для события " + sensorEvent.getPayloadCase());
        }
        handler.handle(sensorEvent);
    }

}
