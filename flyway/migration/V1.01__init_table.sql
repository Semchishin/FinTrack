CREATE TABLE IF NOT EXISTS fintrack.transaction (
    transaction_id SERIAL PRIMARY KEY,
    amount NUMERIC(15,2) NOT NULL,
    category VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)