CREATE SCHEMA IF NOT EXISTS payments;

CREATE TABLE IF NOT EXISTS payments.payment (
    payment_id UUID PRIMARY KEY,
    order_id UUID,
    products_total DECIMAL(19, 2) NOT NULL,
    fee_total DECIMAL(19, 2) NOT NULL,
    delivery_total DECIMAL(19, 2) NOT NULL,
    total_price DECIMAL(19, 2) NOT NULL,
    payment_state VARCHAR(20)
);