package ru.yandex.practicum.commerce.interaction.cart.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NotAuthorizedUserException extends RuntimeException {
    private String userMessage;
    private HttpStatus httpStatus;

    public NotAuthorizedUserException(HttpStatus httpStatus, String message) {
        super(message);
        userMessage = message;
        this.httpStatus = httpStatus;
    }
}
