ALTER TABLE accounts DROP COLUMN customer_name;

ALTER TABLE accounts ADD COLUMN customer_id BIGINT NOT NULL;

CREATE INDEX idx_customer_id ON accounts(customer_id);