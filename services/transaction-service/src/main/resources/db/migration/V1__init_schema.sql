CREATE TABLE IF NOT EXISTS transaction
(
    id              VARCHAR(255) PRIMARY KEY,
    from_account_id BIGINT,
    to_account_id   BIGINT,
    amount          DOUBLE PRECISION,
    status          VARCHAR(20) DEFAULT 'PENDING',
    description     VARCHAR(255),
    created_at      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transaction_from_account_id ON transaction (from_account_id);
CREATE INDEX idx_transaction_to_account_id ON transaction (to_account_id);
CREATE INDEX idx_transaction_status ON transaction (status);
