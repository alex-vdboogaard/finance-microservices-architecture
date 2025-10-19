package com.finance.transactionservice.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Column(name = "user_id")
    private Long userId;

    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PENDING;

    public enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    public Transaction() {
        // Default constructor required by JPA
    }

    public Transaction(
        Long id,
        Double amount,
        Long userId,
        LocalDateTime timestamp,
        PaymentMethod paymentMethod,
        TransactionStatus status
    ) {
        this.id = id;
        this.amount = amount;
        this.userId = userId;
        this.timestamp = timestamp;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id)
            && Objects.equals(amount, that.amount)
            && Objects.equals(userId, that.userId)
            && Objects.equals(timestamp, that.timestamp)
            && Objects.equals(paymentMethod, that.paymentMethod)
            && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, userId, timestamp, paymentMethod, status);
    }

    @Override
    public String toString() {
        return "Transaction{"
            + "id=" + id
            + ", amount=" + amount
            + ", userId=" + userId
            + ", timestamp=" + timestamp
            + ", paymentMethod=" + paymentMethod
            + ", status=" + status
            + '}';
    }

    public static final class Builder {
        private Long id;
        private Double amount;
        private Long userId;
        private LocalDateTime timestamp;
        private PaymentMethod paymentMethod;
        private TransactionStatus status;
        private boolean timestampSet;
        private boolean statusSet;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder amount(Double amount) {
            this.amount = amount;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            this.timestampSet = true;
            return this;
        }

        public Builder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder status(TransactionStatus status) {
            this.status = status;
            this.statusSet = true;
            return this;
        }

        public Transaction build() {
            Transaction transaction = new Transaction();
            transaction.setId(id);
            transaction.setAmount(amount);
            transaction.setUserId(userId);
            if (timestampSet) {
                transaction.setTimestamp(timestamp);
            }
            if (statusSet) {
                transaction.setStatus(status);
            }
            transaction.setPaymentMethod(paymentMethod);
            return transaction;
        }
    }
}
