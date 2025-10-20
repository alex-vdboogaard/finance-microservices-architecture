package com.finance.audit_log_service.dto;

import java.time.LocalDateTime;

public record AuditLogResponse(
    Long id,
    String action,
    LocalDateTime timestamp
) {}
