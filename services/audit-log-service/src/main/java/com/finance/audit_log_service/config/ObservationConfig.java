package com.finance.audit_log_service.config;

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
