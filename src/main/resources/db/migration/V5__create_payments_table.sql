CREATE TABLE payments
(
    id                     BIGSERIAL PRIMARY KEY,
    payment_reference      VARCHAR(50)    NOT NULL,
    source_account_id      BIGINT         NOT NULL,
    destination_account_id BIGINT         NOT NULL,
    amount                 NUMERIC(19, 2) NOT NULL,
    currency               VARCHAR(5)     NOT NULL,
    status                 VARCHAR(20)    NOT NULL,
    version                BIGINT         NOT NULL DEFAULT 0,
    created_at             TIMESTAMP      NOT NULL,
    updated_at             TIMESTAMP,

    CONSTRAINT uk_payments_payment_reference UNIQUE (payment_reference),

    CONSTRAINT chk_payment_amount_positive CHECK (amount > 0),

    CONSTRAINT chk_payment_different_accounts CHECK (source_account_id <> destination_account_id)

);

CREATE INDEX idx_payment_source_account ON payments (source_account_id);

CREATE INDEX idx_payment_destination_account on payments (destination_account_id);

CREATE INDEX idx_payment_status ON payments (status);