package com.finance.accountservice.dto;

import java.time.LocalDateTime;

public record AccountResponse(
        Long id,
        String accountNumber,
        Double balance,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

}
