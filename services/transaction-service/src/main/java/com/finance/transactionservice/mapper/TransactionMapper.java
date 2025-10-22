package com.finance.transactionservice.mapper;

import com.finance.common.model.CompletedTransaction;
import com.finance.transactionservice.dto.PaymentMethodResponse;
import com.finance.transactionservice.dto.TransactionResponse;
import com.finance.transactionservice.model.PaymentMethod;
import com.finance.transactionservice.model.Transaction;

public final class TransactionMapper {
    private TransactionMapper() {
    }

    public static TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getUserId(),
                transaction.getTimestamp(),
                transaction.getStatus(),
                toPaymentMethodResponse(transaction.getPaymentMethod()));
    }

    private static PaymentMethodResponse toPaymentMethodResponse(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        return new PaymentMethodResponse(
                paymentMethod.getId(),
                paymentMethod.getName(),
                paymentMethod.getDescription());
    }

    public static CompletedTransaction toCompletedTransaction(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return new CompletedTransaction(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getUserId(),
                transaction.getTimestamp(),
                toCompletedPaymentMethod(transaction.getPaymentMethod()),
                toCompletedTransactionStatus(transaction.getStatus()));
    }

    private static CompletedTransaction.PaymentMethod toCompletedPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        return new CompletedTransaction.PaymentMethod(
                paymentMethod.getId(),
                paymentMethod.getName(),
                paymentMethod.getDescription());
    }

    private static CompletedTransaction.TransactionStatus toCompletedTransactionStatus(Transaction.TransactionStatus status) {
        if (status == null) {
            return CompletedTransaction.TransactionStatus.PENDING;
        }
        return CompletedTransaction.TransactionStatus.valueOf(status.name());
    }
}
