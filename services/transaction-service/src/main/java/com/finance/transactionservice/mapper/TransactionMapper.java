package com.finance.transactionservice.mapper;

import com.finance.common.dto.TransferEventDTO;
import com.finance.transactionservice.model.Transaction;
import java.time.LocalDateTime;

public class TransactionMapper {

    public static Transaction toEntity(TransferEventDTO dto) {
        return Transaction.builder()
                .transactionId(dto.transactionId())
                .fromAccountId(dto.fromAccountId())
                .toAccountId(dto.toAccountId())
                .amount(dto.amount())
                .status(Transaction.TransactionStatus.valueOf(dto.status()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static TransferEventDTO toDTO(Transaction entity) {
        return new TransferEventDTO(
                entity.getTransactionId(),
                entity.getFromAccountId(),
                entity.getToAccountId(),
                entity.getAmount(),
                entity.getStatus().name(),
                entity.getUpdatedAt());
    }
}
