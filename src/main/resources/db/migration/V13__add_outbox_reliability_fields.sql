ALTER TABLE outbox_events
    ADD COLUMN next_attempt_at TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN locked_by VARCHAR(100),
    ADD COLUMN locked_until TIMESTAMP,
    ADD COLUMN last_error TEXT,
    ADD COLUMN updated_at TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP;

CREATE INDEX idx_outbox_claimable
    ON outbox_events (status, next_attempt_at, created_at);

CREATE INDEX idx_outbox_lease_expiry
    ON outbox_events (locked_until)
    WHERE locked_until IS NOT NULL;
