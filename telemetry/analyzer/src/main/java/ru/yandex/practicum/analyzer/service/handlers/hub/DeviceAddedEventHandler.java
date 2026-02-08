package ru.yandex.practicum.analyzer.service.handlers.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.mapper.EntityMapper;
import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedEventHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    public String getType() {
        return "DeviceAddedEventAvro";
    }

    @Override
    public void handle(HubEventAvro event) {
        Sensor sensor = EntityMapper.hubEventAvroToSensorEntity(event);
        log.info("[Device added event handler] обработка события добавления устройства id={}", sensor.getId());
        if (!sensorRepository.existsByIdInAndHubId(List.of(sensor.getId()), event.getHubId())) {
            log.info("[Device added event handler] сохранение в базу устройства id={}", sensor.getId());
            sensorRepository.save(sensor);
        }
    }
}
