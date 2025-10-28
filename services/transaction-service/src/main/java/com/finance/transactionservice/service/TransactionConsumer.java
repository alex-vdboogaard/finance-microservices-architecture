package com.finance.transactionservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.finance.common.dto.TransferEventDTO;
import com.finance.transactionservice.model.Transaction;

@Service
public class TransactionConsumer {
    private final TransactionService transactionService;

    public TransactionConsumer(TransactionService transactionService, TransactionProducer producer) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "transaction.completed", groupId = "transaction-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransactionCompleted(TransferEventDTO transaction) {
        transactionService.updateStatus(transaction, Transaction.TransactionStatus.SUCCESS);
    }

    @KafkaListener(topics = "transaction.failed", groupId = "transaction-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransactionFailed(TransferEventDTO transaction) {
        transactionService.updateStatus(transaction, Transaction.TransactionStatus.FAILED);
    }

}
