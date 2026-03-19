package ru.yandex.practicum.commerce.interaction.delivery.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NoDeliveryFoundException extends RuntimeException {
    private String userMessage;
    private HttpStatus httpStatus;

    public NoDeliveryFoundException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        userMessage = message;
    }
}
