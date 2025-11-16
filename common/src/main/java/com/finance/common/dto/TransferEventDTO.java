package com.finance.common.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record TransferEventDTO(
        String transactionId,
        Long fromAccountId,
        Long toAccountId,
        Double amount,
        String status,
        String description,
        LocalDateTime timestamp) implements Serializable {

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
