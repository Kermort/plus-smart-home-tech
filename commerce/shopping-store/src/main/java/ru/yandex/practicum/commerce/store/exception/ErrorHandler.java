package ru.yandex.practicum.commerce.store.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.interaction.general.exception.ErrorResponse;
import ru.yandex.practicum.commerce.interaction.store.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.interaction.general.exception.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException e) {
        log.warn("{} [{}]", e.getHttpStatus(), e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .userMessage(e.getUserMessage())
                .httpStatus(e.getHttpStatus().toString())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        log.warn("{} [{}]", HttpStatus.BAD_REQUEST, e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .userMessage(e.getUserMessage())
                .httpStatus(e.getHttpStatus().toString())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }
}
