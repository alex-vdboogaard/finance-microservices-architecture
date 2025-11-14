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
-- Table: "user"
-- =====================================
CREATE TABLE IF NOT EXISTS "user" (
    user_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    goverment_id VARCHAR(10) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL DEFAULT 'default'
);

-- =====================================
-- Table: account
-- =====================================
CREATE TABLE IF NOT EXISTS account (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES "user"(user_id) ON DELETE CASCADE,
    account_number VARCHAR(30) UNIQUE NOT NULL,
    balance DECIMAL(12, 2) NOT NULL CHECK (balance >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
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
-- Sample seed data for users
-- =====================================
INSERT INTO "user" (user_id, first_name, last_name, email, goverment_id, password)
VALUES
    (1, 'Alice', 'Johnson', 'alice.johnson@example.com', 'GOV0000001', 'password1'),
    (2, 'Bob', 'Smith', 'bob.smith@example.com', 'GOV0000002', 'password2'),
    (3, 'Charlie', 'Brown', 'charlie.brown@example.com', 'GOV0000003', 'password3'),
    (4, 'Diana', 'Miller', 'diana.miller@example.com', 'GOV0000004', 'password4'),
    (5, 'Ethan', 'Davis', 'ethan.davis@example.com', 'GOV0000005', 'password5'),
    (6, 'Fiona', 'Wilson', 'fiona.wilson@example.com', 'GOV0000006', 'password6'),
    (7, 'George', 'Taylor', 'george.taylor@example.com', 'GOV0000007', 'password7'),
    (8, 'Hannah', 'Anderson', 'hannah.anderson@example.com', 'GOV0000008', 'password8'),
    (9, 'Ian', 'Thomas', 'ian.thomas@example.com', 'GOV0000009', 'password9'),
    (10, 'Julia', 'Moore', 'julia.moore@example.com', 'GOV0000010', 'password10'),
    (11, 'Kevin', 'Martin', 'kevin.martin@example.com', 'GOV0000011', 'password11'),
    (12, 'Laura', 'Jackson', 'laura.jackson@example.com', 'GOV0000012', 'password12'),
    (13, 'Michael', 'White', 'michael.white@example.com', 'GOV0000013', 'password13'),
    (14, 'Nina', 'Harris', 'nina.harris@example.com', 'GOV0000014', 'password14'),
    (15, 'Oliver', 'Clark', 'oliver.clark@example.com', 'GOV0000015', 'password15'),
    (16, 'Paula', 'Lewis', 'paula.lewis@example.com', 'GOV0000016', 'password16'),
    (17, 'Quentin', 'Walker', 'quentin.walker@example.com', 'GOV0000017', 'password17'),
    (18, 'Rachel', 'Hall', 'rachel.hall@example.com', 'GOV0000018', 'password18'),
    (19, 'Steven', 'Allen', 'steven.allen@example.com', 'GOV0000019', 'password19'),
    (20, 'Tina', 'Young', 'tina.young@example.com', 'GOV0000020', 'password20')
ON CONFLICT (email) DO NOTHING;

-- =====================================
-- Sample seed data for accounts (2 per user)
-- =====================================
INSERT INTO account (user_id, account_number, balance)
VALUES
    (1, 'ACC-1001', 5200.00),
    (1, 'ACC-1002', 1500.50),
    (2, 'ACC-2001', 7800.00),
    (2, 'ACC-2002', 250.75),
    (3, 'ACC-3001', 12000.00),
    (3, 'ACC-3002', 500.00),
    (4, 'ACC-4001', 3400.00),
    (4, 'ACC-4002', 50.00),
    (5, 'ACC-5001', 9800.00),
    (5, 'ACC-5002', 120.00),
    (6, 'ACC-6001', 4300.00),
    (6, 'ACC-6002', 900.00),
    (7, 'ACC-7001', 15000.00),
    (7, 'ACC-7002', 2000.25),
    (8, 'ACC-8001', 6200.00),
    (8, 'ACC-8002', 75.30),
    (9, 'ACC-9001', 2500.00),
    (9, 'ACC-9002', 125.00),
    (10, 'ACC-10001', 8300.00),
    (10, 'ACC-10002', 600.00),
    (11, 'ACC-11001', 4600.00),
    (11, 'ACC-11002', 310.10),
    (12, 'ACC-12001', 9100.00),
    (12, 'ACC-12002', 420.00),
    (13, 'ACC-13001', 7200.00),
    (13, 'ACC-13002', 50.00),
    (14, 'ACC-14001', 3800.00),
    (14, 'ACC-14002', 200.00),
    (15, 'ACC-15001', 5600.00),
    (15, 'ACC-15002', 900.00),
    (16, 'ACC-16001', 4400.00),
    (16, 'ACC-16002', 120.00),
    (17, 'ACC-17001', 10500.00),
    (17, 'ACC-17002', 300.00),
    (18, 'ACC-18001', 6900.00),
    (18, 'ACC-18002', 50.00),
    (19, 'ACC-19001', 8100.00),
    (19, 'ACC-19002', 230.00),
    (20, 'ACC-20001', 7200.00),
    (20, 'ACC-20002', 80.00)
ON CONFLICT (account_number) DO NOTHING;
