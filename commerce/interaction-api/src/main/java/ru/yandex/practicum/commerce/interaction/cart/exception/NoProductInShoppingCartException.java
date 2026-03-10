package ru.yandex.practicum.commerce.interaction.cart.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NoProductInShoppingCartException extends RuntimeException {
    private String userMessage;
    private HttpStatus httpStatus;

    public NoProductInShoppingCartException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        userMessage = message;
    }
}
