package com.finance.audit_log_service.dto.event;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuditEvent(
        String transactionId,
        Long fromAccountId,
        Long toAccountId,
        Double amount,
        String status,
        String description,
        LocalDateTime timestamp) implements Serializable {

    public AuditEvent(
            String transactionId,
            Long fromAccountId,
            Long toAccountId,
            Double amount,
            String status,
            LocalDateTime timestamp) {
        this(transactionId, fromAccountId, toAccountId, amount, status, null, timestamp);
    }
}
