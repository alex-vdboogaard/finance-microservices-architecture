package com.finance.transactionservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferRequestDTO(

                @NotNull @Positive @DecimalMax("100000.00") Double amount,

                @NotNull @Positive Long fromAccountId,

                @NotNull @Positive Long toAccountId) {
}
