package ru.yandex.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.analyzer.model.ScenarioAction;

import java.util.List;

@Repository
public interface ScenarioActionsRepository extends JpaRepository<ScenarioAction, ScenarioActionsCompositeKey> {
    List<ScenarioAction> findByScenarioId(Long scenarioId);
}
