package com.finance.common.dto;

import java.time.LocalDateTime;

public record TransferEventDTO(
        String transactionId,
        Long fromAccountId,
        Long toAccountId,
        Double amount,
        String status,
        String description,
        LocalDateTime timestamp) {

    public TransferEventDTO(
            String transactionId,
            Long fromAccountId,
            Long toAccountId,
            Double amount,
            String status,
            LocalDateTime timestamp) {
        this(transactionId, fromAccountId, toAccountId, amount, status, null, timestamp);
    }
}
