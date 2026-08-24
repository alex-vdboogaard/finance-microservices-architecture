package com.finance.notification_service.service;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finance.notification_service.dto.CreateNotificationRequest;
import com.finance.notification_service.dto.NotificationResponse;
import com.finance.notification_service.model.Notification;
import com.finance.notification_service.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("Should return all notifications from repository")
    void shouldReturnAllNotifications() {
        // Given
        Notification n1 = Notification.builder().id(1L).userId(100L).title("Title 1").description("Desc 1").build();
        Notification n2 = Notification.builder().id(2L).userId(200L).title("Title 2").description("Desc 2").build();
        when(repository.findAll()).thenReturn(List.of(n1, n2));

        // When
        List<Notification> result = notificationService.getAll();

        // Then
        assertThat(result).hasSize(2).containsExactly(n1, n2);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no notifications exist")
    void shouldReturnEmptyListWhenNoNotificationsExist() {
        // Given
        when(repository.findAll()).thenReturn(List.of());

        // When
        List<Notification> result = notificationService.getAll();

        // Then
        assertThat(result).isEmpty();
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should propagate exception when repository fails during getAll")
    void shouldThrowExceptionWhenRepositoryFailsOnGetAll() {
        // Given
        when(repository.findAll()).thenThrow(new RuntimeException("Database connection timeout"));

        // When / Then
        assertThatThrownBy(() -> notificationService.getAll())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database connection timeout");
    }

    @Test
    @DisplayName("Should return notifications by user ID")
    void shouldReturnNotificationsByUserId() {
        // Given
        Long userId = 100L;
        Notification n1 = Notification.builder().id(1L).userId(userId).title("Title 1").description("Desc 1").build();
        when(repository.findByUserId(userId)).thenReturn(List.of(n1));

        // When
        List<Notification> result = notificationService.findByUserId(userId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
        verify(repository).findByUserId(userId);
    }

    @Test
    @DisplayName("Should return empty list when user has no notifications")
    void shouldReturnEmptyListWhenUserHasNoNotifications() {
        // Given
        Long nonExistentUserId = 999L;
        when(repository.findByUserId(nonExistentUserId)).thenReturn(List.of());

        // When
        List<Notification> result = notificationService.findByUserId(nonExistentUserId);

        // Then
        assertThat(result).isEmpty();
        verify(repository).findByUserId(nonExistentUserId);
    }

    @Test
    @DisplayName("Should propagate exception when repository fails during findByUserId")
    void shouldThrowExceptionWhenRepositoryFailsOnFindByUserId() {
        // Given
        Long userId = 100L;
        when(repository.findByUserId(userId)).thenThrow(new RuntimeException("DB query error"));

        // When / Then
        assertThatThrownBy(() -> notificationService.findByUserId(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DB query error");
    }

    @Test
    @DisplayName("Should create and return notification response")
    void shouldCreateNotification() {
        // Given
        CreateNotificationRequest request = new CreateNotificationRequest(100L, "New Alert", "Alert details");
        Notification savedNotification = Notification.builder()
                .id(1L)
                .userId(100L)
                .title("New Alert")
                .description("Alert details")
                .timestamp(LocalDateTime.now())
                .build();

        when(repository.save(any(Notification.class))).thenReturn(savedNotification);

        // When
        NotificationResponse response = notificationService.createNotification(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.userId()).isEqualTo(100L);
        assertThat(response.title()).isEqualTo("New Alert");
        assertThat(response.description()).isEqualTo("Alert details");
        assertThat(response.timestamp()).isEqualTo(savedNotification.getTimestamp());

        verify(repository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Should propagate exception when repository fails during createNotification save")
    void shouldThrowExceptionWhenRepositoryFailsOnSave() {
        // Given
        CreateNotificationRequest request = new CreateNotificationRequest(100L, "New Alert", "Alert details");
        when(repository.save(any(Notification.class))).thenThrow(new RuntimeException("Disk full or DB constraint violation"));

        // When / Then
        assertThatThrownBy(() -> notificationService.createNotification(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Disk full or DB constraint violation");
    }
}
