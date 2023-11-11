CREATE TABLE shopping_list (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    time_act TIMESTAMP,
    user_id INT NOT NULL
);

CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    quantity INT NOT NULL,
    shopping_list_id INT,
    category_id INT NOT NULL
);

CREATE TABLE category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL
);

CREATE TABLE application_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(250) NOT NULL,
    last_name VARCHAR(250) NOT NULL,
    email VARCHAR(250) NOT NULL
);

CREATE TABLE user_role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(250),
    user_id INT NOT NULL
);


ALTER TABLE product
    ADD CONSTRAINT product_shoppinglist_id_fk
    FOREIGN KEY (shopping_list_id) REFERENCES shopping_list(id);

ALTER TABLE product
    ADD CONSTRAINT product_category_id_fk
    FOREIGN KEY (category_id) REFERENCES category(id);

ALTER TABLE shopping_list
    ADD CONSTRAINT shopping_list_user_id_fk
    FOREIGN KEY (user_id) REFERENCES application_user(id);

ALTER TABLE user_role
    ADD CONSTRAINT user_role_user_id_fk
    FOREIGN KEY (user_id) REFERENCES application_user(id);