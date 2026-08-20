CREATE TABLE IF NOT EXISTS audit_log
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    action    VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_log_action ON audit_log (action);
CREATE INDEX idx_audit_log_timestamp ON audit_log (timestamp);
