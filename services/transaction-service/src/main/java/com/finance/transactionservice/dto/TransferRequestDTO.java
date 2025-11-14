package com.finance.transactionservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferRequestDTO(

        @NotNull(message = "Amount is required") @Positive(message = "Amount must be greater than zero") @DecimalMax(value = "100000.00", message = "Amount cannot exceed 100 000") Double amount,

        @NotNull(message = "From account ID is required") @Positive(message = "From account ID must be a positive number") Long fromAccountId,

        @NotNull(message = "To account ID is required") @Positive(message = "To account ID must be a positive number") Long toAccountId) {
}
