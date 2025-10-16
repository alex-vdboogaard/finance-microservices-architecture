package com.finance.audit_log_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.audit_log_service.model.AuditLog;
import com.finance.audit_log_service.repository.AuditLogRepository;

@Service
public class AuditLogService {
    @Autowired
    private AuditLogRepository auditLogRepository;

    public List<AuditLog> getAll() {
        return auditLogRepository.findAll();
    }

    public AuditLog createAuditLog(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    public List<AuditLog> findByAction(String action) {
        return auditLogRepository.findAll().stream().filter(log -> log.getAction().toLowerCase().contains(action.toLowerCase())).collect(Collectors.toList());
    }
}
