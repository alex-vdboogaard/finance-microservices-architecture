package com.finance.common.dto;

import java.time.LocalDateTime;

public record TransactionStatusUpdateDTO(
                String transactionId,
                String status,
                String message,
                LocalDateTime updatedAt) {
}
