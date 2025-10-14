package com.finance.audit_log_service.repository;

import com.finance.audit_log_service.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByAction(String action);
    //TODO: add filter by search query
}
