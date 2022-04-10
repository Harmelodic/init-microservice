CREATE TABLE IF NOT EXISTS `account`
(
    `id` uuid NOT NULL,
    `name` VARCHAR (255) NOT NULL,
    `customer_id` uuid NOT NULL,
    PRIMARY KEY (id)
);
