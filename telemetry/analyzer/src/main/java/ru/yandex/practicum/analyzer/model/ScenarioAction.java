package ru.yandex.practicum.analyzer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.yandex.practicum.analyzer.repository.ScenarioActionsCompositeKey;

@Entity
@Table(name = "scenario_actions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ScenarioActionsCompositeKey.class)
public class ScenarioAction {
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
    @JoinColumn(name = "action_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Action action;
}
