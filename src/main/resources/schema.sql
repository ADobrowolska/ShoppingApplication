CREATE TABLE Shopping_List (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    time_act TIMESTAMP
);

CREATE TABLE Product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    quantity INT NOT NULL,
    shopping_list_id INT,
    category_id INT
);

CREATE TABLE Category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL
);


ALTER TABLE Product
    ADD CONSTRAINT product_shoppinglist_id_fk
    FOREIGN KEY (shopping_list_id) REFERENCES Shopping_List(id);

ALTER TABLE Product
    ADD CONSTRAINT product_category_id_fk
    FOREIGN KEY (category_id) REFERENCES Category(id);
