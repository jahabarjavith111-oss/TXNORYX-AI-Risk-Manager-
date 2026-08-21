-- Use the txnoryx database
USE txnoryx;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255),
    role VARCHAR(50),
    created_at DATETIME,
    PRIMARY KEY (id)
);

-- Create transactions table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    transaction_id VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT,
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    payment_method VARCHAR(50),
    merchant VARCHAR(255),
    status VARCHAR(50),
    failure_reason VARCHAR(255),
    device_id VARCHAR(255),
    location VARCHAR(255),
    created_at DATETIME,
    PRIMARY KEY (id)
);

-- Create transaction_events table
CREATE TABLE IF NOT EXISTS transaction_events (
    id BIGINT NOT NULL AUTO_INCREMENT,
    transaction_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    timestamp DATETIME NOT NULL,
    PRIMARY KEY (id)
);

-- Add foreign key from transactions to users (optional but recommended)
ALTER TABLE transactions ADD CONSTRAINT fk_transactions_users
FOREIGN KEY (user_id) REFERENCES users(id);