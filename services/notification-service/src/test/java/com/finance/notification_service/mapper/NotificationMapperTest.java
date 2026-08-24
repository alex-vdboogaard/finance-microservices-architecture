package com.finance.notification_service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.finance.notification_service.dto.NotificationResponse;
import com.finance.notification_service.model.Notification;

class NotificationMapperTest {

    @Test
    @DisplayName("Should correctly map Notification entity to NotificationResponse DTO")
    void shouldMapEntityToResponse() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Notification entity = Notification.builder()
                .id(10L)
                .userId(20L)
                .title("Transfer Success")
                .description("Successfully transferred R100")
                .timestamp(now)
                .build();

        // When
        NotificationResponse response = NotificationMapper.toResponse(entity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.userId()).isEqualTo(20L);
        assertThat(response.title()).isEqualTo("Transfer Success");
        assertThat(response.description()).isEqualTo("Successfully transferred R100");
        assertThat(response.timestamp()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should correctly map Notification entity with null optional fields")
    void shouldMapEntityWithNullFields() {
        // Given
        Notification entity = Notification.builder()
                .id(null)
                .userId(50L)
                .title("Alert without description")
                .description(null)
                .timestamp(null)
                .build();

        // When
        NotificationResponse response = NotificationMapper.toResponse(entity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNull();
        assertThat(response.userId()).isEqualTo(50L);
        assertThat(response.title()).isEqualTo("Alert without description");
        assertThat(response.description()).isNull();
        assertThat(response.timestamp()).isNull();
    }
}
