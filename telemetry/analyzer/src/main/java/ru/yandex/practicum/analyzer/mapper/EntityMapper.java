package ru.yandex.practicum.analyzer.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.analyzer.model.Condition;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.kafka.telemetry.event.*;

@UtilityClass
public class EntityMapper {
    public static Sensor hubEventAvroToSensorEntity(HubEventAvro event) {
        DeviceAddedEventAvro deviceAddedEvent = (DeviceAddedEventAvro) event.getPayload();
        return Sensor.builder()
                .id(deviceAddedEvent.getId())
                .hubId(event.getHubId())
                .build();
    }

    public static Scenario hubEventAvroToScenarioEntity(HubEventAvro event) {
        return Scenario.builder()
                .hubId(event.getHubId())
                .name(((ScenarioAddedEventAvro) event.getPayload()).getName())
                .build();
    }

    public static Condition conditionAvroToEntity(ScenarioConditionAvro condition) {
        return Condition.builder()
                .type(condition.getType())
                .operation(condition.getOperation())
                .value(resolveValue(condition.getValue()))
                .build();
    }

    public static Action actionAvroToEntity(DeviceActionAvro action) {
        return Action.builder()
                .type(action.getType())
                .value(action.getValue())
                .build();
    }

    private Integer resolveValue(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return (Boolean) value ? 1 : 0;
        }
    }
}
