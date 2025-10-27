package com.finance.audit_log_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.finance.audit_log_service.dto.CreateAuditLogRequest;
import com.finance.common.dto.TransferEventDTO;

@Service
public class TransactionConsumer {
    private final AuditLogService auditLogService;

    public TransactionConsumer(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @KafkaListener(topics = "${app.kafka.topics.transaction-initiated}", groupId = "audit-log-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransactionInitiated(TransferEventDTO transaction) {
        CreateAuditLogRequest log = new CreateAuditLogRequest("New transfer initiated: " + transaction.toString());
        auditLogService.createAuditLog(log);
    }

    @KafkaListener(topics = "${app.kafka.topics.transaction-completed}", groupId = "audit-log-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransactionCompleted(TransferEventDTO transaction) {
        CreateAuditLogRequest log = new CreateAuditLogRequest("Transfer completed: " + transaction.toString());
        auditLogService.createAuditLog(log);
    }

    @KafkaListener(topics = "${app.kafka.topics.transaction-failed}", groupId = "audit-log-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransactionFailed(TransferEventDTO transaction) {
        CreateAuditLogRequest log = new CreateAuditLogRequest("Transfer failed: " + transaction.toString());
        auditLogService.createAuditLog(log);
    }
}
