package ru.yandex.practicum.commerce.interaction.payment.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {
    private String userMessage;
    private HttpStatus httpStatus;

    public NotEnoughInfoInOrderToCalculateException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        userMessage = message;
    }
}
