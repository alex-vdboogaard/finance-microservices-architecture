package com.finance.transactionservice.config;

import io.micrometer.observation.ObservationPredicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ObservationConfig {

    @Bean
    public ObservationPredicate noJdbcConnectionObservationPredicate() {
        return (name, context) -> !"jdbc.connection".equals(name);
    }
}
