CREATE SCHEMA IF NOT EXISTS warehouse;

CREATE TABLE IF NOT EXISTS warehouse.products (
    product_id UUID PRIMARY KEY,
    fragile BOOLEAN NOT NULL,
    width DECIMAL(10, 2) NOT NULL,
    height DECIMAL(10, 2) NOT NULL,
    depth DECIMAL(10, 2) NOT NULL,
    weight DECIMAL(10, 3) NOT NULL,
    quantity INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS warehouse.booking (
    product_id UUID NOT NULL,
    order_id UUID NOT NULL,
    delivery_id UUID,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (order_id, product_id)
);