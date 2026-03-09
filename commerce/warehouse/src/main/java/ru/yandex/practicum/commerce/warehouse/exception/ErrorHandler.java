package ru.yandex.practicum.commerce.warehouse.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.interaction.general.exception.ErrorResponse;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleSpecifiedProductAlreadyInWarehouseException(
            SpecifiedProductAlreadyInWarehouseException e) {
        log.warn("{} [{}]", e.getHttpStatus(), e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .userMessage(e.getUserMessage())
                .httpStatus(e.getHttpStatus().toString())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    public ResponseEntity<ErrorResponse> handleProductInShoppingCartLowQuantityInWarehouseException(
            ProductInShoppingCartLowQuantityInWarehouse e) {
        log.warn("{} [{}]", e.getHttpStatus(), e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .userMessage(e.getUserMessage())
                .httpStatus(e.getHttpStatus().toString())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleNoSpecifiedProductInWarehouseException(
            NoSpecifiedProductInWarehouseException e) {
        log.warn("{} [{}]", e.getHttpStatus(), e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .userMessage(e.getUserMessage())
                .httpStatus(e.getHttpStatus().toString())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }
}
