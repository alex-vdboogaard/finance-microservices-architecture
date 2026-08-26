package com.finance.transactionservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.finance.transactionservice.dto.event.TransferEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionProducer {

        private final KafkaTemplate<String, TransferEvent> kafkaTemplate;

        // Topic names
        private final String transactionInitiatedTopic = "transaction.initiated";

        public TransactionProducer(KafkaTemplate<String, TransferEvent> kafkaTemplate) {
                this.kafkaTemplate = kafkaTemplate;
        }

        public void sendTransactionInitiated(TransferEvent event) {
                publishEvent(transactionInitiatedTopic, "initiated", event);
        }

        private void publishEvent(String topic, String label, TransferEvent event) {
                log.info("Publishing {} transaction event: transactionId={}, topic={}",
                                label, event.transactionId(), topic);

                kafkaTemplate.send(topic, event)
                                .thenAccept(result -> log.debug(
                                                "Kafka ack for transactionId={}, topic={}, partition={}, offset={}",
                                                event.transactionId(),
                                                topic,
                                                result.getRecordMetadata().partition(),
                                                result.getRecordMetadata().offset()))
                                .exceptionally(ex -> {
                                        log.error("Failed to publish transactionId={} to topic={}",
                                                         event.transactionId(), topic, ex);
                                        return null;
                                });
        }
}

