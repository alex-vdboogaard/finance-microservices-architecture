package com.finance.notification_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.finance.common.dto.TransferEventDTO;
import com.finance.notification_service.dto.CreateNotificationRequest;

@Service
public class TransactionConsumer {
    private final NotificationService notificationService;

    public TransactionConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "${app.kafka.topics.transaction-completed}", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransactionCompleted(TransferEventDTO transaction) {
        String title = "Transaction Completed";
        String description = transaction.description();
        Long userId = transaction.fromAccountId();
        notificationService.createNotification(new CreateNotificationRequest(userId, title, description));
    }

    @KafkaListener(topics = "${app.kafka.topics.transaction-failed}", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransactionFailed(TransferEventDTO transaction) {
        String title = "Transaction Failed";
        String description = "Transfer failed: " + transaction.description();
        Long userId = transaction.fromAccountId();
        notificationService.createNotification(new CreateNotificationRequest(userId, title, description));
    }
}
