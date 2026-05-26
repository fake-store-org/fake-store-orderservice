CREATE TABLE orders
(
    order_id          UUID PRIMARY KEY                  DEFAULT gen_random_uuid(),
    buyer_id          UUID                     NOT NULL,
    order_sum         NUMERIC(19, 2)           NOT NULL,
    order_status      VARCHAR(30)              NOT NULL,
    stripe_session_id VARCHAR(255),
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE,

    first_name        VARCHAR(255),
    last_name         VARCHAR(255),
    co                VARCHAR(255),
    street_name       VARCHAR(255),
    street_name2      VARCHAR(255),
    postal_code       VARCHAR(5),
    city              VARCHAR(255),
    country           VARCHAR(255)
);

CREATE TABLE order_items
(
    order_item_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id     UUID           NOT NULL,
    title          VARCHAR(255)   NOT NULL,
    price_per_item NUMERIC(19, 2) NOT NULL,
    quantity       INTEGER        NOT NULL,
    order_id       UUID           NOT NULL,

    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id)
        REFERENCES orders (order_id) ON DELETE CASCADE
);

CREATE INDEX idx_order_items_order_id ON order_items (order_id);