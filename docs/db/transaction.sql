-- ===============================
-- Create database (if not exists)
-- ===============================
DO
$$
BEGIN
    IF NOT EXISTS (
        SELECT FROM pg_database WHERE datname = 'transaction_db'
    ) THEN
        PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE transaction_db');
    END IF;
END
$$;

-- ===============================
-- Create table: payment_method
-- ===============================
CREATE TABLE IF NOT EXISTS payment_method (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    CONSTRAINT payment_method_name_check CHECK (name IN ('CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'PAYPAL', 'CASH'))
);

-- ===============================
-- Seed table: payment_method
-- ===============================
INSERT INTO payment_method (name, description) VALUES
    ('CREDIT_CARD', 'Transactions funded with a credit card'),
    ('DEBIT_CARD', 'Transactions funded with a debit card'),
    ('BANK_TRANSFER', 'Direct bank transfer payments'),
    ('PAYPAL', 'Payments processed via PayPal'),
    ('CASH', 'Cash-based transactions recorded in system')
ON CONFLICT (name) DO NOTHING;

-- ===============================
-- Create table: transaction
-- ===============================
CREATE TABLE IF NOT EXISTS transaction (
    id SERIAL PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    user_id BIGINT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method_id INTEGER NOT NULL REFERENCES payment_method(id),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT transaction_status_check CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED'))
);

-- ===============================
-- Insert example data (100 rows)
-- ===============================
INSERT INTO transaction (amount, user_id, timestamp, payment_method_id, status)
SELECT
    -- Random amount between 10 and 10,000
    ROUND((RANDOM() * 9990 + 10)::numeric, 2),

    -- Random user_id between 1 and 20
    (FLOOR(RANDOM() * 20) + 1)::BIGINT,

    -- Random timestamp within the last 30 days
    NOW() - (FLOOR(RANDOM() * 30) || ' days')::INTERVAL,

    -- Random payment method seeded above
    pm.id,

    -- Random status from allowed values
    (ARRAY['PENDING', 'COMPLETED', 'FAILED', 'CANCELLED'])[1 + (RANDOM() * 3)::int]
FROM generate_series(1, 100)
CROSS JOIN LATERAL (
    SELECT id
    FROM payment_method
    ORDER BY RANDOM()
    LIMIT 1
) pm;
