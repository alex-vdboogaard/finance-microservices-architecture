package com.finance.accountservice.exception;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finance.common.dto.ErrorResponse;
import com.finance.common.dto.ErrorResponse.FieldError;

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
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .errors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CloneNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUser(CloneNotSupportedException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .type(URI.create("https://api.finance.com/problems/duplicate-user"))
                .title("Conflict")
                .status(HttpStatus.CONFLICT.value())
                .detail("User with this email already exists")
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .type(URI.create("https://api.finance.com/problems/user-not-found"))
                .title("Not found")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .type(URI.create("https://api.finance.com/problems/account-not-found"))
                .title("Not found")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAmountException(InvalidAmountException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .type(URI.create("https://api.finance.com/problems/invalid-amount"))
                .title("Bad Request")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
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
