CREATE TABLE vending_user
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255)          NOT NULL,
    deposit  INT DEFAULT 0,
    role     INT                   NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uc_user_username UNIQUE (username)
);

CREATE TABLE product
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    amount_available INT                   NOT NULL,
    cost             INT                   NOT NULL,
    product_name     VARCHAR(255)          NOT NULL,
    seller_id        BIGINT                NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id),
    CONSTRAINT fk_product_seller_id FOREIGN KEY (seller_id) REFERENCES vending_user(id)
);