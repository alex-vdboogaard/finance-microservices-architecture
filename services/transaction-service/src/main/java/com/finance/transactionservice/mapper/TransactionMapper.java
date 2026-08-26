package com.finance.transactionservice.mapper;

import java.time.LocalDateTime;

import com.finance.transactionservice.dto.TransactionResponseDTO;
import com.finance.transactionservice.dto.event.TransferEvent;
import com.finance.transactionservice.model.Transaction;

public class TransactionMapper {

    public static Transaction toEntity(TransferEvent dto) {
        return Transaction.builder()
                .id(dto.transactionId())
                .fromAccountId(dto.fromAccountId())
                .toAccountId(dto.toAccountId())
                .amount(dto.amount())
                .status(Transaction.TransactionStatus.valueOf(dto.status()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static TransactionResponseDTO toResponseDTO(Transaction entity) {
        return new TransactionResponseDTO(
                entity.getId(),
                entity.getFromAccountId(),
                entity.getToAccountId(),
                entity.getAmount(),
                entity.getStatus().name(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static TransferEvent toEvent(Transaction entity) {
        return new TransferEvent(
                entity.getId(),
                entity.getFromAccountId(),
                entity.getToAccountId(),
                entity.getAmount(),
                entity.getStatus().name(),
                entity.getDescription(),
                entity.getUpdatedAt());
    }
}

