package com.finance.audit_log_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.finance.audit_log_service.dto.CreateAuditLogRequest;
import com.finance.common.model.CompletedTransaction;

@Service
public class TransactionConsumer {
    @Autowired
    AuditLogService auditLogService;

    @KafkaListener(topics = "${app.kafka.topics.financial-transactions}", groupId = "transaction-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransaction(CompletedTransaction transaction) {
        CreateAuditLogRequest log = new CreateAuditLogRequest("New transaction: " + transaction.toString());
        auditLogService.createAuditLog(log);
    }
}
