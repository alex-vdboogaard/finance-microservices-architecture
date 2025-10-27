package com.finance.common.dto;

import java.time.LocalDateTime;

public record AccountDebitResult(
        String transactionId,
        Long accountId,
        Double amount,
        String status,
        LocalDateTime timestamp) {
}
