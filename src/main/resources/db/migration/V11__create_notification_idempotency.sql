CREATE TABLE notification_idempotency
(
    id                BIGSERIAL PRIMARY KEY,
    notification_key  VARCHAR(200) NOT NULL,
    payment_reference VARCHAR(50)  NOT NULL,
    notification_type VARCHAR(50)  NOT NULL,
    channel           VARCHAR(20)  NOT NULL,
    status            VARCHAR(20)  NOT NULL,
    created_at        TIMESTAMP    NOT NULL,
    updated_at        TIMESTAMP    NOT NULL,
    CONSTRAINT uk_notification_idempotency_key UNIQUE (notification_key),
    CONSTRAINT chk_notification_idempotency_status CHECK (status IN ('PROCESSING', 'COMPLETED', 'FAILED'))
);
CREATE INDEX idx_notification_idempotency_payment_reference ON notification_idempotency (payment_reference);
CREATE INDEX idx_notification_idempotency_created_at ON notification_idempotency (created_at);