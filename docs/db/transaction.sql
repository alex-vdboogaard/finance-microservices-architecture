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

-- Connect to the database
\c transaction_db;

-- ===============================
-- Create table: transaction
-- ===============================
CREATE TABLE IF NOT EXISTS transaction (
    id SERIAL PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    user_id BIGINT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- Insert example data (100 rows)
-- ===============================
INSERT INTO transaction (amount, user_id, timestamp)
SELECT
    -- Random amount between 10 and 10,000
    ROUND((RANDOM() * 9990 + 10)::numeric, 2),

    -- Random user_id between 1 and 20
    (FLOOR(RANDOM() * 20) + 1)::BIGINT,

    -- Random timestamp within the last 30 days
    NOW() - (FLOOR(RANDOM() * 30) || ' days')::INTERVAL
FROM generate_series(1, 100);