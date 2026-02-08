package ru.yandex.practicum.analyzer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.yandex.practicum.analyzer.repository.ScenarioConditionsCompositeKey;

@Entity
@Table(name = "scenario_conditions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ScenarioConditionsCompositeKey.class)
public class ScenarioCondition {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Scenario scenario;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Sensor sensor;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Condition condition;
}
