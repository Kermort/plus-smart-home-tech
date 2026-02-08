package ru.yandex.practicum.analyzer.service.handlers.snapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.mapper.ActionMapper;
import ru.yandex.practicum.analyzer.model.*;
import ru.yandex.practicum.analyzer.repository.*;
import ru.yandex.practicum.analyzer.service.handlers.HubRouterClient;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Map;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotHandler {
    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final ScenarioConditionsRepository scenarioConditionsRepository;
    private final ScenarioActionsRepository scenarioActionsRepository;
    private final HubRouterClient hubRouterClient;

    @Transactional
    public void handleSnapshot(SensorsSnapshotAvro snapshot) {
        log.info("[Snapshot handler] обработка снапшота snapshot={}", snapshot);
        Map<String, SensorStateAvro> sensorState = snapshot.getSensorsState();
        List<Scenario> hubScenarios = scenarioRepository.findByHubId(snapshot.getHubId());

        hubScenarios.stream()
                .filter(scenario -> checkScenario(scenario, sensorState))
                .forEach(this::sendAction);
    }

    private void sendAction(Scenario scenario) {
        log.info("[Snapshot handler] отправка действия для сценария {}", scenario);
        List<ScenarioAction> scenarioActions = scenarioActionsRepository.findByScenarioId(scenario.getId());
        scenarioActions.forEach(
                sa -> hubRouterClient.sendRequest(
                        ActionMapper.toDeviceActionProto(sa.getAction(), sa.getSensor(), sa.getScenario())
                ));

    }

    private boolean checkScenario(Scenario scenario, Map<String, SensorStateAvro> sensorsState) {
        log.info("[Snapshot handler] проверка сценария scenario name={}", scenario.getName());
        List<ScenarioCondition> scenarioConditions = scenarioConditionsRepository.findByScenarioId(scenario.getId());
        return scenarioConditions.stream().allMatch(sc -> checkCondition(sc, sensorsState));
    }

    private boolean checkCondition(ScenarioCondition scenarioCondition, Map<String, SensorStateAvro> sensorsState) {
        Condition condition = scenarioCondition.getCondition();
        log.info("[Snapshot handler] проверка условия conditionType={}", condition.getType());
        SensorStateAvro sensorStateAvro = sensorsState.get(scenarioCondition.getSensor().getId());

        if (sensorStateAvro == null) return false;

        switch (condition.getType()) {
            case SWITCH -> {
                SwitchSensorAvro switchSensor = (SwitchSensorAvro) sensorStateAvro.getData();
                return checkValue(condition, switchSensor.getState() ? 1 : 0);
            }
            case MOTION -> {
                MotionSensorAvro motionSensor = (MotionSensorAvro) sensorStateAvro.getData();
                return checkValue(condition, motionSensor.getMotion() ? 1 : 0);
            }
            case HUMIDITY -> {
                ClimateSensorAvro humiditySensor = (ClimateSensorAvro) sensorStateAvro.getData();
                return checkValue(condition, humiditySensor.getHumidity());
            }
            case TEMPERATURE -> {
                ClimateSensorAvro temperatureSensor = (ClimateSensorAvro) sensorStateAvro.getData();
                return checkValue(condition, temperatureSensor.getTemperatureC());
            }
            case LUMINOSITY -> {
                LightSensorAvro lightSensor = (LightSensorAvro) sensorStateAvro.getData();
                return checkValue(condition, lightSensor.getLuminosity());
            }
            case CO2LEVEL -> {
                ClimateSensorAvro co2Sensor = (ClimateSensorAvro) sensorStateAvro.getData();
                return checkValue(condition, co2Sensor.getCo2Level());
            }

            default -> {
                return false;
            }
        }
    }

    private boolean checkValue(Condition condition, Integer value) {
        Integer conditionValue = condition.getValue();

        switch (condition.getOperation()) {
            case EQUALS -> {
                return Objects.equals(value, conditionValue);
            }
            case GREATER_THAN -> {
                return value > conditionValue;
            }
            case LOWER_THAN -> {
                return value < conditionValue;
            }
            default -> {
                return false;
            }
        }
    }

}
