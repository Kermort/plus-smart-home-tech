package ru.yandex.practicum.commerce.payment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.interaction.general.exception.ErrorResponse;
import ru.yandex.practicum.commerce.interaction.general.exception.ValidationException;
import ru.yandex.practicum.commerce.interaction.payment.exception.NoPaymentFoundException;
import ru.yandex.practicum.commerce.interaction.payment.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.commerce.interaction.warehouse.exception.NoSpecifiedProductInWarehouseException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NoPaymentFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoPaymentFoundException(NoPaymentFoundException e) {
        log.warn("{} [{}]", e.getHttpStatus(), e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .userMessage(e.getUserMessage())
                .httpStatus(e.getHttpStatus().toString())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(NotEnoughInfoInOrderToCalculateException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughInfoInOrderToCalculateException(NotEnoughInfoInOrderToCalculateException e) {
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
