CREATE SCHEMA IF NOT EXISTS store;

CREATE TABLE IF NOT EXISTS store.products
(
    product_id UUID PRIMARY KEY,
    product_name VARCHAR NOT NULL,
    description TEXT NOT NULL,
    image_src VARCHAR,
    quantity_state VARCHAR(20) NOT NULL,
    product_state VARCHAR(20) NOT NULL,
    product_category VARCHAR(20),
    price NUMERIC(10, 2) NOT NULL
);