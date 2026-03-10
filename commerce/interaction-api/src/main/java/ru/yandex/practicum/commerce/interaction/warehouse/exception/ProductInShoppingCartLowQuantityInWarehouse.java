package ru.yandex.practicum.commerce.interaction.warehouse.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {
    private String userMessage;
    private HttpStatus httpStatus;

    public ProductInShoppingCartLowQuantityInWarehouse(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        userMessage = message;
    }
}
