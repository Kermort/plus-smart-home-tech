package ru.yandex.practicum.analyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioActionsCompositeKey {
    private Long scenario;
    private String sensor;
    private Long action;
}
