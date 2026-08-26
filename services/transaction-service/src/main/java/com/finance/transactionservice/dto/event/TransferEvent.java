package com.finance.transactionservice.dto.event;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TransferEvent(
        String transactionId,
        Long fromAccountId,
        Long toAccountId,
        Double amount,
        String status,
        String description,
        LocalDateTime timestamp) implements Serializable {

    public TransferEvent(
            String transactionId,
            Long fromAccountId,
            Long toAccountId,
            Double amount,
            String status,
            LocalDateTime timestamp) {
        this(transactionId, fromAccountId, toAccountId, amount, status, null, timestamp);
    }
}
