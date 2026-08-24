package com.finance.notification_service.service;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finance.common.dto.TransferEventDTO;
import com.finance.notification_service.dto.CreateNotificationRequest;
import com.finance.notification_service.dto.NotificationResponse;

@ExtendWith(MockitoExtension.class)
class TransactionConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TransactionConsumer transactionConsumer;

    @Test
    @DisplayName("Should create notification when transaction completed event is consumed")
    void shouldConsumeTransactionCompleted() {
        // Given
        TransferEventDTO event = new TransferEventDTO(
                "tx-123",
                101L,
                202L,
                150.0,
                "COMPLETED",
                "Payment for groceries",
                LocalDateTime.now()
        );

        when(notificationService.createNotification(any(CreateNotificationRequest.class)))
                .thenReturn(new NotificationResponse(1L, 101L, "Transaction Completed", "Payment for groceries", LocalDateTime.now()));

        // When
        transactionConsumer.consumeTransactionCompleted(event);

        // Then
        ArgumentCaptor<CreateNotificationRequest> captor = ArgumentCaptor.forClass(CreateNotificationRequest.class);
        verify(notificationService).createNotification(captor.capture());

        CreateNotificationRequest captured = captor.getValue();
        assertThat(captured.userId()).isEqualTo(101L);
        assertThat(captured.title()).isEqualTo("Transaction Completed");
        assertThat(captured.description()).isEqualTo("Payment for groceries");
    }

    @Test
    @DisplayName("Should propagate exception when notification service fails on transaction completed event")
    void shouldPropagateExceptionWhenServiceFailsOnTransactionCompleted() {
        // Given
        TransferEventDTO event = new TransferEventDTO(
                "tx-123",
                101L,
                202L,
                150.0,
                "COMPLETED",
                "Payment for groceries",
                LocalDateTime.now()
        );

        when(notificationService.createNotification(any(CreateNotificationRequest.class)))
                .thenThrow(new RuntimeException("Database error during notification creation"));

        // When / Then
        assertThatThrownBy(() -> transactionConsumer.consumeTransactionCompleted(event))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error during notification creation");
    }

    @Test
    @DisplayName("Should create notification when transaction failed event is consumed")
    void shouldConsumeTransactionFailed() {
        // Given
        TransferEventDTO event = new TransferEventDTO(
                "tx-124",
                101L,
                202L,
                500.0,
                "FAILED",
                "Insufficient funds",
                LocalDateTime.now()
        );

        when(notificationService.createNotification(any(CreateNotificationRequest.class)))
                .thenReturn(new NotificationResponse(2L, 101L, "Transaction Failed", "Transfer failed: Insufficient funds", LocalDateTime.now()));

        // When
        transactionConsumer.consumeTransactionFailed(event);

        // Then
        ArgumentCaptor<CreateNotificationRequest> captor = ArgumentCaptor.forClass(CreateNotificationRequest.class);
        verify(notificationService).createNotification(captor.capture());

        CreateNotificationRequest captured = captor.getValue();
        assertThat(captured.userId()).isEqualTo(101L);
        assertThat(captured.title()).isEqualTo("Transaction Failed");
        assertThat(captured.description()).isEqualTo("Transfer failed: Insufficient funds");
    }

    @Test
    @DisplayName("Should propagate exception when notification service fails on transaction failed event")
    void shouldPropagateExceptionWhenServiceFailsOnTransactionFailed() {
        // Given
        TransferEventDTO event = new TransferEventDTO(
                "tx-124",
                101L,
                202L,
                500.0,
                "FAILED",
                "Insufficient funds",
                LocalDateTime.now()
        );

        when(notificationService.createNotification(any(CreateNotificationRequest.class)))
                .thenThrow(new RuntimeException("Service failure"));

        // When / Then
        assertThatThrownBy(() -> transactionConsumer.consumeTransactionFailed(event))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service failure");
    }

    @Test
    @DisplayName("Should handle event with null description without throwing NullPointerException")
    void shouldHandleNullDescriptionInEvent() {
        // Given
        TransferEventDTO event = new TransferEventDTO(
                "tx-125",
                101L,
                202L,
                50.0,
                "FAILED",
                null,
                LocalDateTime.now()
        );

        when(notificationService.createNotification(any(CreateNotificationRequest.class)))
                .thenReturn(new NotificationResponse(3L, 101L, "Transaction Failed", "Transfer failed: null", LocalDateTime.now()));

        // When
        transactionConsumer.consumeTransactionFailed(event);

        // Then
        ArgumentCaptor<CreateNotificationRequest> captor = ArgumentCaptor.forClass(CreateNotificationRequest.class);
        verify(notificationService).createNotification(captor.capture());
        assertThat(captor.getValue().description()).isEqualTo("Transfer failed: null");
    }
}
