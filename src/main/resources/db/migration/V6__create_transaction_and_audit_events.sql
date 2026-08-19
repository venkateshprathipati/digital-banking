-- ============================================================
-- 1. TRANSACTIONS
-- ============================================================
CREATE TABLE transactions
(
    id                    BIGSERIAL PRIMARY KEY,
    transaction_reference VARCHAR(40)    NOT NULL,
    account_id            BIGINT         NOT NULL,
    transaction_type      VARCHAR(30)    NOT NULL,
    amount                NUMERIC(19, 2) NOT NULL,
    currency              VARCHAR(3)     NOT NULL,
    status                VARCHAR(20)    NOT NULL,
    description           VARCHAR(255),
    created_at            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_transactions_reference UNIQUE (transaction_reference),

    CONSTRAINT chk_transaction_amount CHECK ( currency IN ('INR', 'USD', 'EUR', 'GBP')),

    CONSTRAINT chk_transaction_type CHECK ( transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER', 'PAYMENT','REVERSAL', 'REFUND') ),

    CONSTRAINT chk_transaction_status CHECK ( status IN ('PENDING', 'SUCCESS', 'FAILED', 'REVERSED' ))
);

CREATE INDEX idx_transaction_account_created ON transactions (account_id, created_at DESC);

CREATE INDEX idx_transaction_account_type ON transactions (account_id, transaction_type);

-- ============================================================
-- 2. AUDIT EVENTS
-- ============================================================
CREATE TABLE audit_events
(
    id             BIGSERIAL PRIMARY KEY,
    event_type     VARCHAR(60)  NOT NULL,
    actor_id       VARCHAR(100),
    actor_type     VARCHAR(30),
    resource_type  VARCHAR(40)  NOT NULL,
    resource_id    VARCHAR(100) NOT NULL,
    description    VARCHAR(255),
    ip_address     VARCHAR(45),
    correlation_id VARCHAR(100),
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP

);

CREATE INDEX idx_audit_resource ON audit_events (resource_type,resource_id);

CREATE INDEX idx_audit_actor ON audit_events (actor_id);

CREATE INDEX idx_audit_created ON audit_events (created_at DESC);