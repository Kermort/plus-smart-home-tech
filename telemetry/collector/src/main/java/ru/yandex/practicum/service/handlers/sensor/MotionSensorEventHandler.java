package ru.yandex.practicum.service.handlers.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.enums.SensorEventType;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.mapper.sensor.MotionSensorEventMapper;
import ru.yandex.practicum.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;

@Component
@RequiredArgsConstructor
public class MotionSensorEventHandler implements SensorEventHandler {
    @Value("${collector.kafka.producer.topics.sensors-events}")
    private String sensorEventTopic;
    private final KafkaClient kafkaClient;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        kafkaClient.send(sensorEventTopic,
                event.getHubId(),
                MotionSensorEventMapper.toAvro((MotionSensorEvent) event));
    }
}
