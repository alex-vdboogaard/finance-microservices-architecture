package com.finance.transactionservice.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.finance.common.model.CompletedTransaction;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, CompletedTransaction> producerFactory(
            KafkaProperties kafkaProperties,
            ObjectProvider<SslBundles> sslBundles) {
        return new DefaultKafkaProducerFactory<>(
                kafkaProperties.buildProducerProperties(sslBundles.getIfAvailable()));
    }

    @Bean
    public KafkaTemplate<String, CompletedTransaction> kafkaTemplate(
            ProducerFactory<String, CompletedTransaction> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
