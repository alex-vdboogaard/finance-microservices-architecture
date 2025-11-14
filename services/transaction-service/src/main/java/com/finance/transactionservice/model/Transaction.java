package com.finance.transactionservice.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String transactionId;
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum TransactionStatus {
        PENDING,
        SUCCESS,
        FAILED
    }

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
