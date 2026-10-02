CREATE TABLE IF NOT EXISTS customers (
    id             UUID           NOT NULL PRIMARY KEY,
    name           VARCHAR(255)   NOT NULL,
    age            INTEGER        NOT NULL,
    email          VARCHAR(80)    NOT NULL UNIQUE,
    account_number VARCHAR(255)   NOT NULL UNIQUE,
    branch         VARCHAR(255)   NOT NULL,
    bank_name      VARCHAR(255)   NOT NULL,
    balance        NUMERIC(19, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
    id               UUID           NOT NULL PRIMARY KEY,
    amount           NUMERIC(19, 2) NOT NULL,
    timestamp        TIMESTAMP(6)   NOT NULL,
    transaction_type VARCHAR(255)   NOT NULL,
    sender_id        UUID REFERENCES customers (id),
    receiver_id      UUID REFERENCES customers (id)
);

CREATE INDEX IF NOT EXISTS idx_transactions_sender ON transactions (sender_id);
CREATE INDEX IF NOT EXISTS idx_transactions_receiver ON transactions (receiver_id);
