package ru.yandex.practicum.service.handlers.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.enums.SensorEventType;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.mapper.sensor.LightSensorEventMapper;
import ru.yandex.practicum.model.sensor.LightSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;

@Component
@RequiredArgsConstructor
public class LightSensorEventHandler implements SensorEventHandler {
    @Value("${collector.kafka.producer.topics.sensors-events}")
    private String sensorEventTopic;
    private final KafkaClient kafkaClient;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        kafkaClient.send(sensorEventTopic,
                event.getHubId(),
                event.getTimestamp(),
                LightSensorEventMapper.toAvro((LightSensorEvent) event));
    }
}
