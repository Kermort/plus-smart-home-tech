CREATE SCHEMA IF NOT EXISTS carts;

CREATE TABLE IF NOT EXISTS carts.cart (
    cart_id UUID PRIMARY KEY,
    username VARCHAR NOT NULL,
    cart_state VARCHAR(20) NOT NULL,
    UNIQUE(cart_id, username)
);

CREATE TABLE IF NOT EXISTS carts.cart_product (
    cart_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (cart_id, product_id),
    FOREIGN KEY (cart_id) REFERENCES carts.cart(cart_id) ON DELETE CASCADE
);