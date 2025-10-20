package com.finance.audit_log_service.mapper;

import com.finance.audit_log_service.dto.AuditLogResponse;
import com.finance.audit_log_service.model.AuditLog;

public final class AuditLogMapper {
    private AuditLogMapper(){};

    public static AuditLogResponse toResponse(AuditLog auditLog) {
        return new AuditLogResponse(
            auditLog.getId(),
            auditLog.getAction(),
            auditLog.getTimestamp()
        );
    }
}
