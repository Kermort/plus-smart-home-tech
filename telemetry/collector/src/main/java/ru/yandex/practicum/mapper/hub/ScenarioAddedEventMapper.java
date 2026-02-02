package ru.yandex.practicum.mapper.hub;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ScenarioAddedEventMapper {
    public static HubEventAvro toAvro(HubEventProto event) {
        ScenarioAddedEventProto scenarioAddedEvent = event.getScenarioAdded();

        List<ScenarioConditionAvro> conditions = scenarioAddedEvent.getConditionList().stream()
                .map(ScenarioConditionMapper::toAvro)
                .collect(Collectors.toList());
        List<DeviceActionAvro> actions = scenarioAddedEvent.getActionList().stream()
                .map(DeviceActionMapper::toAvro)
                .collect(Collectors.toList());

        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setConditions(conditions)
                .setActions(actions)
                .build();

        Instant timestamp = Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos());

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();
    }
}
