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

import com.finance.audit_log_service.model.AuditLog;
import com.finance.audit_log_service.service.AuditLogService;




@RestController
@RequestMapping("/logs")
public class AuditLogController {
    @Autowired
    private AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(auditLogService.getAll());
    }

    @GetMapping("/action")
    public ResponseEntity<List<AuditLog>> getAuditLogsByAction(@RequestParam String action) {
        return ResponseEntity.ok(auditLogService.findByAction(action));
    }
    

    @PostMapping
    public ResponseEntity<AuditLog> createAuditLog(@RequestBody AuditLog auditLog) {
       AuditLog created = auditLogService.createAuditLog(auditLog);
       return ResponseEntity.ok(created);
    }
}
