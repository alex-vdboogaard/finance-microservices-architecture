package com.finance.transactionservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.finance.common.dto.TransferEventDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionProducer {

        private final KafkaTemplate<String, TransferEventDTO> kafkaTemplate;

        // Topic names
        private final String transactionInitiatedTopic = "transaction.initiated";
        private final String transactionCompletedTopic = "transaction.completed";
        private final String transactionFailedTopic = "transaction.failed";

        public TransactionProducer(KafkaTemplate<String, TransferEventDTO> kafkaTemplate) {
                this.kafkaTemplate = kafkaTemplate;
        }

        public void sendTransactionInitiated(TransferEventDTO event) {
                publishEvent(transactionInitiatedTopic, "initiated", event);
        }

        private void publishEvent(String topic, String label, TransferEventDTO event) {
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
