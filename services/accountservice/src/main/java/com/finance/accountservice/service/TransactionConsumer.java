package com.finance.accountservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.finance.accountservice.dto.event.TransferEvent;

@Service
public class TransactionConsumer {
    private final AccountService accountService;
    private final TransactionProducer producer;

    public TransactionConsumer(AccountService accountService, TransactionProducer producer) {
        this.accountService = accountService;
        this.producer = producer;
    }

    @KafkaListener(id = "account-transaction-initiated-listener", topics = "transaction.initiated", groupId = "account-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransactionInitiated(TransferEvent t) {
        TransferEvent transfer = accountService.transferMoney(t);
        if ("SUCCESS".equals(transfer.status())) {
            producer.sendTransactionCompleted(transfer);
        } else {
            producer.sendTransactionFailed(transfer);
        }
    }

}

