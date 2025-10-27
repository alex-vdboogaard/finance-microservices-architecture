package com.finance.common.dto;

public record TransferRequestDTO(
        Long fromAccountId,
        Long toAccountId,
        Double amount) {
}
