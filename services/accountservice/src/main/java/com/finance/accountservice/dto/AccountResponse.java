package com.finance.accountservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record AccountResponse(
        Long id,
        String accountNumber,
        Double balance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) implements Serializable {

}
