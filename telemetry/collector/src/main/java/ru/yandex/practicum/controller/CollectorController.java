package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.service.CollectorService;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class CollectorController {
    private final CollectorService collectorService;

    @PostMapping("/hubs")
    public void handleHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        collectorService.collectHubEvent(hubEvent);
    }

    @PostMapping("/sensors")
    public void handleSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        collectorService.collectSensorEvent(sensorEvent);
    }
}
