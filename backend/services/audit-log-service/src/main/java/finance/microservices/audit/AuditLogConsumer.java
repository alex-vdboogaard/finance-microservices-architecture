
package finance.microservices.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuditLogConsumer {

    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    public AuditLogConsumer(AuditLogService auditLogService, ObjectMapper objectMapper) {
        this.auditLogService = auditLogService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "audit-log", groupId = "audit-log-group")
    public void consume(String message) throws IOException {
        AuditLog auditLog = objectMapper.readValue(message, AuditLog.class);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogService.saveAuditLog(auditLog);
    }
}
