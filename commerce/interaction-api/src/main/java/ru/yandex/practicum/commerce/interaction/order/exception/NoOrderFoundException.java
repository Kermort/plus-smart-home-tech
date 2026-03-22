package ru.yandex.practicum.commerce.interaction.order.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NoOrderFoundException extends RuntimeException {
    private String userMessage;
    private HttpStatus httpStatus;

    public NoOrderFoundException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        userMessage = message;
    }
}
