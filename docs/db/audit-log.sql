-- Drop database if it exists
DROP DATABASE IF EXISTS audit_db;

-- Create the database
CREATE DATABASE audit_db;

-- Use the new database
USE audit_db;

-- Create the audit_log table
CREATE TABLE audit_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    action VARCHAR(255) NOT NULL,
    timestamp DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Optional: insert sample data
INSERT INTO audit_log (action) VALUES 
('User logged in'),
('Transaction created'),
('Audit log entry example');
