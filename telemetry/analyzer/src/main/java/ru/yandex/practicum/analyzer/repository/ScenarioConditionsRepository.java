package ru.yandex.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.analyzer.model.ScenarioCondition;

import java.util.List;

@Repository
public interface ScenarioConditionsRepository extends JpaRepository<ScenarioCondition, ScenarioConditionsCompositeKey> {
    List<ScenarioCondition> findByScenarioId(Long scenarioId);
}
