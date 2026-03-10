package ru.yandex.practicum.commerce.interaction.store.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ProductNotFoundException extends RuntimeException {
    private String userMessage;
    private HttpStatus httpStatus;

    public ProductNotFoundException(HttpStatus httpStatus, String message) {
        super(message);
        userMessage = message;
        this.httpStatus = httpStatus;
    }
}
