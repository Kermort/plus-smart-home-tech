package ru.yandex.practicum.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Properties;

@Slf4j
@Configuration
public class AggregatorKafkaConsumerConfig {

    @Value("${aggregator.kafka.consumer.properties.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${aggregator.kafka.consumer.properties.group-id}")
    private String groupId;

    @Value("${aggregator.kafka.consumer.properties.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${aggregator.kafka.consumer.properties.enable-auto-commit}")
    private String enableAutoCommit;

    @Value("${aggregator.kafka.consumer.properties.key-deserializer}")
    private String keyDeserializer;

    @Value("${aggregator.kafka.consumer.properties.value-deserializer}")
    private String valueDeserializer;


    @Bean
    KafkaConsumer<String, SensorEventAvro> kafkaConsumer() {
        log.info("Настройка потребителя");
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

        return new KafkaConsumer<>(config);
    }
}
