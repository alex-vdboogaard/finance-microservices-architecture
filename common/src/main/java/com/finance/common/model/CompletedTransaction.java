package com.finance.common.model;

import java.time.LocalDateTime;

public record CompletedTransaction(
        Long id,
        Double amount,
        Long userId,
        LocalDateTime timestamp,
        PaymentMethod paymentMethod,
        TransactionStatus status) {

    public CompletedTransaction {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (status == null) {
            status = TransactionStatus.PENDING;
        }
    }

    public enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    public record PaymentMethod(
            Long id,
            String name,
            String description) {
    }
}
