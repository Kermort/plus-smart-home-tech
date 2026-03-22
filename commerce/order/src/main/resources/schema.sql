CREATE SCHEMA IF NOT EXISTS orders;

CREATE TABLE IF NOT EXISTS orders.order (
    order_id UUID PRIMARY KEY,
    username VARCHAR NOT NULL,
    order_state VARCHAR(20) NOT NULL,
    cart_id UUID,
    delivery_id UUID,
    payment_id UUID,
    delivery_volume DECIMAL(19, 2),
    delivery_weight DECIMAL(19, 3),
    fragile BOOLEAN,
    total_price DECIMAL(19, 2),
    products_price DECIMAL(19, 2),
    delivery_price DECIMAL(19, 2)
);

CREATE TABLE IF NOT EXISTS orders.order_product (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders.order(order_id) ON DELETE CASCADE
);