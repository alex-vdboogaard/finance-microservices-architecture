package com.finance.audit_log_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=none"
})
class AuditLogServiceApplicationTests {
    @Test
    void contextLoads() {
    }
}

