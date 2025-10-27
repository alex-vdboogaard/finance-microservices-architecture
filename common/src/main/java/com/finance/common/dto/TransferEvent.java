package com.finance.common.dto;

import java.time.LocalDateTime;

public record TransferEvent(
        String transactionId,
        Long fromAccountId,
        Long toAccountId,
        Double amount,
        String status,
        LocalDateTime timestamp) {
}
