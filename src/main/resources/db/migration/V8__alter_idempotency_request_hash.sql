ALTER TABLE payment_idempotency
    ADD COLUMN IF NOT EXISTS request_hash VARCHAR(64);