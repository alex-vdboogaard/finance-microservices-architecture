package com.finance.transactionservice.dto;

import jakarta.validation.constraints.NotNull;

public record CreateTransactionRequest(
    @NotNull Double amount,
    @NotNull Long UserId,
    @NotNull Long paymentMethodId,
    @NotNull String status
) {}
