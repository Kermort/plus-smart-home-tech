package ru.yandex.practicum.analyzer.service.handlers.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.mapper.EntityMapper;
import ru.yandex.practicum.analyzer.model.*;
import ru.yandex.practicum.analyzer.repository.*;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;
    private final SensorRepository sensorRepository;
    private final ScenarioConditionsRepository scenarioConditionsRepository;
    private final ScenarioActionsRepository scenarioActionsRepository;

    @Override
    public String getType() {
        return "ScenarioAddedEventAvro";
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        log.info("[Scenario added event handler] обработка события добавления сценария");
        List<ScenarioCondition> scenarioConditionList = new ArrayList<>();
        List<ScenarioAction> scenarioActionList = new ArrayList<>();
        Scenario scenarioEntity = EntityMapper.hubEventAvroToScenarioEntity(event);

        Scenario scenario = scenarioRepository.findByHubIdAndName(event.getHubId(), scenarioEntity.getName())
                .orElseGet(() -> scenarioRepository.save(scenarioEntity));
        log.info("[Scenario added event handler] найден сценарий id={}", scenario.getId());

        ScenarioAddedEventAvro scenarioEvent = (ScenarioAddedEventAvro) event.getPayload();

        for (ScenarioConditionAvro sc: scenarioEvent.getConditions()) {
            log.info("[Scenario added event handler] сохранение условия для устройства id={}", sc.getSensorId());
            Condition condition = conditionRepository.save(EntityMapper.conditionAvroToEntity(sc));
            Optional<Sensor> sensorOpt = sensorRepository.findByIdAndHubId(sc.getSensorId(), event.getHubId());

            sensorOpt.ifPresent(sensor -> scenarioConditionList.add(new ScenarioCondition(scenario, sensor, condition)));
        }

        for (DeviceActionAvro da: scenarioEvent.getActions()) {
            Action action = actionRepository.save(EntityMapper.actionAvroToEntity(da));
            Optional<Sensor> sensorOpt = sensorRepository.findByIdAndHubId(da.getSensorId(), event.getHubId());

            sensorOpt.ifPresent(sensor -> scenarioActionList.add(new ScenarioAction(scenario, sensor, action)));
        }

        log.info("[Scenario added event handler] scenarioConditionsList={}", scenarioConditionList);
        scenarioConditionsRepository.saveAll(scenarioConditionList);
        log.info("[Scenario added event handler] scenarioActionsList={}", scenarioActionList);
        scenarioActionsRepository.saveAll(scenarioActionList);
    }
}
