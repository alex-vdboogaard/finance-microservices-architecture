package com.finance.common.dto;

public record TransferRequest(
                Long fromAccountId,
                Long toAccountId,
                Double amount) {
}
