package ru.yandex.practicum.commerce.order.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.interaction.cart.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.interaction.general.exception.ErrorResponse;
import ru.yandex.practicum.commerce.interaction.general.exception.ValidationException;
import ru.yandex.practicum.commerce.interaction.order.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotAuthorizedUserException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorizedUserException(NotAuthorizedUserException e) {
        log.warn("{} [{}]", e.getHttpStatus(), e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .userMessage(e.getUserMessage())
                .httpStatus(e.getHttpStatus().toString())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleNoSpecifiedProductInWarehouseException(NoSpecifiedProductInWarehouseException e) {
        log.warn("{} [{}]", e.getHttpStatus(), e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .userMessage(e.getUserMessage())
                .httpStatus(e.getHttpStatus().toString())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    public ResponseEntity<ErrorResponse> handleProductInShoppingCartLowQuantityInWarehouse(ProductInShoppingCartLowQuantityInWarehouse e) {
        log.warn("{} [{}]", e.getHttpStatus(), e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .userMessage(e.getUserMessage())
                .httpStatus(e.getHttpStatus().toString())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(NoOrderFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoOrderFoundException(NoOrderFoundException e) {
        log.warn("{} [{}]", e.getHttpStatus(), e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .userMessage(e.getUserMessage())
                .httpStatus(e.getHttpStatus().toString())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        log.warn("{} [{}]", e.getHttpStatus(), e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .userMessage(e.getUserMessage())
                .httpStatus(e.getHttpStatus().toString())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

}
