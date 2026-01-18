package ru.yandex.practicum.mapper.hub;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.model.hub.scenario.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;

@UtilityClass
public class ScenarioConditionMapper {
    public static ScenarioConditionAvro toAvro(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                .setValue(condition.getValue())
                .build();
    }
}