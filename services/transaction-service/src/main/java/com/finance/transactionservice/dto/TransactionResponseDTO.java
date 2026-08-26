package com.finance.transactionservice.dto;

import java.time.LocalDateTime;

public record TransactionResponseDTO(
        String id,
        Long fromAccountId,
        Long toAccountId,
        Double amount,
        String status,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
