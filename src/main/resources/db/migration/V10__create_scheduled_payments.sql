CREATE TABLE scheduled_payments
(
    id                     BIGSERIAL PRIMARY KEY,
    user_id                UUID                     NOT NULL,
    source_account_id      BIGINT                   NOT NULL,
    destination_account_id BIGINT                   NOT NULL,
    amount                 NUMERIC(19, 2)           NOT NULL,
    currency               VARCHAR(5)               NOT NULL,
    scheduled_at           TIMESTAMP WITH TIME ZONE NOT NULL,
    status                 VARCHAR(20)              NOT NULL,
    payment_reference      VARCHAR(50),
    failure_reason         VARCHAR(500),
    version                BIGINT                   NOT NULL DEFAULT 0,
    created_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at             TIMESTAMP WITH TIME ZONE,

    CONSTRAINT chk_scheduled_payment_amount_positive CHECK ( amount > 0 ),

    CONSTRAINT chk_scheduled_payment_different_accounts CHECK ( source_account_id <> destination_account_id )
);

CREATE INDEX idx_scheduled_payment_user_id ON scheduled_payments (user_id);
CREATE INDEX idx_scheduled_payment_due ON scheduled_payments (status, scheduled_at);
CREATE INDEX idx_scheduled_payment_source_account ON scheduled_payments (source_account_id);
CREATE INDEX idx_scheduled_payment_destination_account ON scheduled_payments (destination_account_id);