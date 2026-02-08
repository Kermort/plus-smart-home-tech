package ru.yandex.practicum.analyzer.service.handlers.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.model.ScenarioAction;
import ru.yandex.practicum.analyzer.model.ScenarioCondition;
import ru.yandex.practicum.analyzer.repository.*;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioRemovedEventHandler implements HubEventHandler {
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;
    private final ScenarioConditionsRepository scenarioConditionsRepository;
    private final ScenarioActionsRepository scenarioActionsRepository;

    @Override
    public String getType() {
        return "ScenarioRemovedEventAvro";
    }

    @Override
    public void handle(HubEventAvro event) {
        log.info("[Scenario removed event handler] обработка события удаления сценария");
        ScenarioRemovedEventAvro scenarioEvent = (ScenarioRemovedEventAvro) event.getPayload();

        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(event.getHubId(), scenarioEvent.getName());

        List<ScenarioCondition> scenarioConditionList = scenarioConditionsRepository.findByScenarioId(scenarioOpt.get().getId());
        List<ScenarioAction> scenarioActionList = scenarioActionsRepository.findByScenarioId(scenarioOpt.get().getId());

        conditionRepository.deleteAllByIdInBatch(scenarioConditionList.stream().map(sc -> sc.getCondition().getId()).toList());
        actionRepository.deleteAllByIdInBatch(scenarioActionList.stream().map(sa -> sa.getAction().getId()).toList());
        scenarioRepository.deleteById(scenarioOpt.get().getId());
    }

}
