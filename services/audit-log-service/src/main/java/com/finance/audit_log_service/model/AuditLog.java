package com.finance.audit_log_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    @Column(name = "timestamp")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public String getAction() {
        return action;
    }
}
