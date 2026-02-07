package ru.yandex.practicum.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.kafka.telemetry.event.serializer.AvroSerializer;

import java.util.Properties;

@Slf4j
@Configuration
public class AggregatorKafkaProducerConfig {
    @Value("${aggregator.kafka.producer.properties.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    KafkaProducer<String, SensorsSnapshotAvro> kafkaProducer() {
        log.info("Настройка продюсера");
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class);
        return new KafkaProducer<>(config);
    }
}
