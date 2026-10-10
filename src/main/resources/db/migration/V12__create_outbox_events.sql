CREATE TABLE outbox_events
(
    id             BIGSERIAL PRIMARY KEY,
    event_id       UUID         NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id   VARCHAR(100) NOT NULL,
    event_type     VARCHAR(150) NOT NULL,
    payload        TEXT         NOT NULL,
    topic          VARCHAR(255) NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    retry_count    INTEGER      NOT NULL DEFAULT 0,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_at   TIMESTAMP NULL,

    CONSTRAINT uk_outbox_event_id UNIQUE (event_id),

    CONSTRAINT ck_outbox_status CHECK ( status IN ('PENDING', 'PUBLISHED', 'FAILED'))
);

CREATE INDEX idx_outbox_pending ON outbox_events (status, created_at);