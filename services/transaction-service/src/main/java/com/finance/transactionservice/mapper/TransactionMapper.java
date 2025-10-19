package com.finance.transactionservice.mapper;

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
            toPaymentMethodResponse(transaction.getPaymentMethod())
        );
    }

    private static PaymentMethodResponse toPaymentMethodResponse(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        return new PaymentMethodResponse(
            paymentMethod.getId(),
            paymentMethod.getName(),
            paymentMethod.getDescription()
        );
    }
}
