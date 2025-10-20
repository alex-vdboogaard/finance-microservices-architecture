package com.finance.transactionservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.finance.transactionservice.config.KafkaTopicsProperties;
import com.finance.transactionservice.dto.TransactionResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionProducer {
    private final KafkaTemplate<String, TransactionResponse> kafkaTemplate;
    private final String transactionTopic;

    public TransactionProducer(
            KafkaTemplate<String, TransactionResponse> kafkaTemplate,
            KafkaTopicsProperties topicsProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.transactionTopic = topicsProperties.transactions();
    }

    public void sendTransaction(TransactionResponse transaction) {
        log.info("Publishing transaction to kafka topic: transaction={}, topic={}", transaction, transactionTopic);
        kafkaTemplate.send(transactionTopic, transaction)
                .thenAccept(result -> log.debug(
                        "Kafka ack for transactionId={}, topic={}, partition={}, offset={}",
                        transaction.id(),
                        transactionTopic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset()))
                .exceptionally(ex -> {
                    log.error("Failed to publish transactionId={} to topic={}", transaction.id(), transactionTopic, ex);
                    return null;
                });
    }
}
