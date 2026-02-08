package ru.yandex.practicum.analyzer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Slf4j
@Configuration
public class AnalyzerKafkaSnapshotConsumerConfig {
    @Value("${analyzer.kafka.properties.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${analyzer.kafka.properties.group-id.snapshots}")
    private String groupId;

    @Value("${analyzer.kafka.properties.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${analyzer.kafka.properties.enable-auto-commit}")
    private String enableAutoCommit;

    @Value("${analyzer.kafka.properties.key-deserializer}")
    private String keyDeserializer;

    @Value("${analyzer.kafka.properties.value-deserializer.snapshots}")
    private String valueDeserializer;

    @Bean
    KafkaConsumer<String, SensorsSnapshotAvro> analyzerSnapshotKafkaConsumer() {
        log.info("[Config] Настройка потребителя для снапшотов");

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
