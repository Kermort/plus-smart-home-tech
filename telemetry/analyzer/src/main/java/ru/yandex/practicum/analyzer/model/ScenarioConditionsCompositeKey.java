package ru.yandex.practicum.analyzer.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioConditionsCompositeKey {
    private Long scenario;
    private String sensor;
    private Long condition;
}
