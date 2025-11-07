package com.finance.audit_log_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finance.audit_log_service.dto.AuditLogResponse;
import com.finance.audit_log_service.dto.CreateAuditLogRequest;
import com.finance.audit_log_service.model.AuditLog;
import com.finance.audit_log_service.service.AuditLogService;
import com.finance.common.dto.ApiResponse;
import com.finance.common.logging.LoggingConfig;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;;

@Slf4j
@RestController
@RequestMapping("/api/v1/logs")
@Tag(name = "Audit Logs", description = "Endpoints for retrieving and creating audit logs")
public class AuditLogController {
    @Autowired
    private AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogs() {
        ApiResponse<List<AuditLog>> response = ApiResponse.<List<AuditLog>>builder()
                .success(true)
                .message("Fetched audit logs successfully")
                .data(auditLogService.getAll())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/action")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogsByAction(@RequestParam String action) {
        ApiResponse<List<AuditLog>> response = ApiResponse.<List<AuditLog>>builder()
                .success(true)
                .message("Fetched audit logs successfully")
                .data(auditLogService.findByAction(action))
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuditLogResponse>> createAuditLog(
            @Valid @RequestBody CreateAuditLogRequest request,
            HttpServletRequest httpRequest) {
        LoggingConfig.startRequest(httpRequest.getRequestURI(), "audit-log-service");

        log.info("Received request to create audit log: action={}", request.action());

        try {
            AuditLogResponse created = auditLogService.createAuditLog(request);
            ApiResponse<AuditLogResponse> response = ApiResponse.<AuditLogResponse>builder()
                    .success(true)
                    .message("Created audit log successfully")
                    .data(created)
                    .build();

            log.info("Audit log created successfully, id={}, action={}, timestamp={}", created.id(),
                    created.action(),
                    created.timestamp());
            LoggingConfig.endRequest();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error saving new audit log: {}", e.getMessage());
            LoggingConfig.endRequest();
            return ResponseEntity.internalServerError().build();
        }

    }

}
