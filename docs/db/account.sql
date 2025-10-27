-- =====================================
-- Database: account_db
-- =====================================
-- Run manually if needed:
-- CREATE DATABASE account_db;
-- \c account_db;

DO
$$
BEGIN
    IF NOT EXISTS (
        SELECT FROM pg_database WHERE datname = 'account_db'
    ) THEN
        EXECUTE 'CREATE DATABASE account_db';
    END IF;
END
$$;


-- =====================================
-- Table: account
-- =====================================
CREATE TABLE IF NOT EXISTS account (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    account_number VARCHAR(30) UNIQUE NOT NULL,
    balance DECIMAL(12, 2) NOT NULL CHECK (balance >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT account_status_check CHECK (
        status IN ('ACTIVE', 'FROZEN', 'CLOSED')
    )
);

-- =====================================
-- Trigger: auto-update updated_at
-- =====================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_update_timestamp ON account;
CREATE TRIGGER trg_update_timestamp
BEFORE UPDATE ON account
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- =====================================
-- Sample seed data
-- =====================================
INSERT INTO account (user_id, account_number, balance, status)
VALUES
    (1, 'ACC-1001', 5000.00, 'ACTIVE'),
    (2, 'ACC-1002', 7500.00, 'ACTIVE'),
    (3, 'ACC-1003', 12000.00, 'FROZEN'),
    (4, 'ACC-1004', 3000.00, 'ACTIVE'),
    (5, 'ACC-1005', 9500.00, 'CLOSED')
ON CONFLICT (account_number) DO NOTHING;