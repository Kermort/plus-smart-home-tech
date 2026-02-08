package ru.yandex.practicum.analyzer.service.handlers.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceRemovedEventHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    public String getType() {
        return "DeviceRemovedEventAvro";
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro deviceRemovedEvent = (DeviceRemovedEventAvro) event.getPayload();
        log.info("[Device removed event handler] обработка события добавления устройства id={}", deviceRemovedEvent.getId());
        sensorRepository.deleteById(deviceRemovedEvent.getId());
    }
}
