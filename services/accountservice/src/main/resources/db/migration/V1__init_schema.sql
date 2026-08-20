CREATE TABLE IF NOT EXISTS users
(
    user_id      BIGSERIAL PRIMARY KEY,
    first_name   VARCHAR(30)  NOT NULL,
    last_name    VARCHAR(50)  NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    goverment_id VARCHAR(10)  NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL DEFAULT 'default'
);

CREATE TABLE IF NOT EXISTS accounts
(
    id             BIGSERIAL PRIMARY KEY,
    user_id        BIGINT       NOT NULL,
    account_number VARCHAR(255) DEFAULT NULL,
    balance        DOUBLE PRECISION DEFAULT NULL,
    status         VARCHAR(50)  DEFAULT 'ACTIVE',
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_accounts_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_goverment_id ON users (goverment_id);
CREATE INDEX idx_accounts_user_id ON accounts (user_id);
CREATE INDEX idx_accounts_account_number ON accounts (account_number);
