package com.finance.notification_service.config;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.finance.common.dto.ErrorResponse;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should handle CustomException and return 400 Bad Request ErrorResponse")
    void shouldHandleCustomException() {
        // Given
        CustomException ex = new CustomException("Invalid notification parameter");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleCustomException(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getType()).isEqualTo(URI.create("https://api.finance.com/problems/custom-error"));
        assertThat(body.getTitle()).isEqualTo("Bad Request");
        assertThat(body.getStatus()).isEqualTo(400);
        assertThat(body.getDetail()).isEqualTo("Invalid notification parameter");
    }

    @Test
    @DisplayName("Should handle generic Exception and return 500 Internal Server Error ErrorResponse")
    void shouldHandleGenericException() {
        // Given
        Exception ex = new RuntimeException("Unexpected database connectivity failure");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getType()).isEqualTo(URI.create("https://api.finance.com/problems/internal-error"));
        assertThat(body.getTitle()).isEqualTo("Internal Server Error");
        assertThat(body.getStatus()).isEqualTo(500);
        assertThat(body.getDetail()).isEqualTo("Unexpected database connectivity failure");
    }
}
