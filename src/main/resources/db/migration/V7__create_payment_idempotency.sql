CREATE TABLE payment_idempotency(
    id BIGSERIAL PRIMARY KEY ,
    user_id UUID NOT NULL ,
    idempotency_key VARCHAR(100) NOT NULL ,
    request_hash CHAR(64) NOT NULL ,
    status VARCHAR(20) NOT NULL ,
    payment_reference VARCHAR(50),
    response_payload TEXT,
    created_at TIMESTAMP NOT NULL ,
    updated_at TIMESTAMP NOT NULL ,

    CONSTRAINT uk_payment_idempotency_user_key
        UNIQUE (user_id, idempotency_key),

    CONSTRAINT chk_payment_idempotency_status
        CHECK ( status IN ('PROCESSING','COMPLETED','FAILED') )
);

CREATE INDEX idx_payment_idempotency_payment_reference
    ON payment_idempotency(payment_reference);

CREATE INDEX idx_payment_idempotency_created_at
    ON payment_idempotency (created_at);