package com.finance.transactionservice.config;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finance.common.dto.ErrorResponse;
import com.finance.common.dto.ErrorResponse.FieldError;

import jakarta.ws.rs.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        var bindingResult = ex.getBindingResult();

        List<FieldError> errors = bindingResult == null
                ? List.of()
                : bindingResult.getFieldErrors()
                        .stream()
                        .map(err -> new ErrorResponse.FieldError(err.getField(), err.getDefaultMessage()))
                        .toList();

        ErrorResponse error = ErrorResponse.builder()
                .type(URI.create("https://api.finance.com/problems/validation"))
                .title("Validation failed")
                .status(400)
                .detail(ex.getMessage())
                .errors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .type(URI.create("https://api.finance.com/problems/resource-not-found"))
                .title("Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = ErrorResponse.builder()
                .type(URI.create("https://api.finance.com/problems/internal-error"))
                .title("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
