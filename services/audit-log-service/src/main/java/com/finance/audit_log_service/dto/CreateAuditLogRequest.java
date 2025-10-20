package com.finance.audit_log_service.dto;

import jakarta.validation.constraints.NotNull;

public record CreateAuditLogRequest(
    @NotNull String action
) {}
