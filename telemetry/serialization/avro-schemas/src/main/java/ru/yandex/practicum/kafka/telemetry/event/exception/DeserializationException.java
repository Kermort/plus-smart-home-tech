package ru.yandex.practicum.kafka.telemetry.event.exception;

public class DeserializationException extends RuntimeException {
    public DeserializationException(String message) {
        super(message);
    }
}
