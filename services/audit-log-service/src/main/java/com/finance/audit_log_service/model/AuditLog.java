package com.finance.audit_log_service.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    public AuditLog() {
        // Default constructor required by JPA
    }

    public AuditLog(Long id, String action, LocalDateTime timestamp) {
        this.id = id;
        this.action = action;
        this.timestamp = timestamp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuditLog auditLog = (AuditLog) o;
        return Objects.equals(id, auditLog.id)
            && Objects.equals(action, auditLog.action)
            && Objects.equals(timestamp, auditLog.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, action, timestamp);
    }

    @Override
    public String toString() {
        return "AuditLog{"
            + "id=" + id
            + ", action='" + action + '\''
            + ", timestamp=" + timestamp
            + '}';
    }

    public static final class Builder {
        private Long id;
        private String action;
        private LocalDateTime timestamp;
        private boolean timestampSet;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            this.timestampSet = true;
            return this;
        }

        public AuditLog build() {
            AuditLog auditLog = new AuditLog();
            auditLog.setId(id);
            auditLog.setAction(action);
            if (timestampSet) {
                auditLog.setTimestamp(timestamp);
            }
            return auditLog;
        }
    }
}
