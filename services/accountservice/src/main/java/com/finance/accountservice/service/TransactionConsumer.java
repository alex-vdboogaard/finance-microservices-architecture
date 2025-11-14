package com.finance.accountservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.finance.common.dto.TransferEventDTO;

@Service
public class TransactionConsumer {
    private final AccountService accountService;
    private final TransactionProducer producer;

    public TransactionConsumer(AccountService accountService, TransactionProducer producer) {
        this.accountService = accountService;
        this.producer = producer;
    }

    @KafkaListener(topics = "transaction.initiated", groupId = "account-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransactionInitiated(TransferEventDTO t) {
        TransferEventDTO transfer = accountService.transferMoney(t);
        if ("SUCCESS".equals(transfer.status())) {
            producer.sendTransactionCompleted(transfer);
        } else {
            producer.sendTransactionFailed(transfer);
        }
    }

}
