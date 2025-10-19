package com.finance.transactionservice.dto;

import java.time.LocalDateTime;

import com.finance.transactionservice.model.Transaction.TransactionStatus;

public record TransactionResponse(
    Long id,
    Double amount,
    Long userId,
    LocalDateTime timestamp,
    TransactionStatus status,
    PaymentMethodResponse paymentMethod
) {
}
