--liquibase formatted sql
--changeset adobrowolska:2

INSERT INTO application_user(first_name, last_name, email) VALUES ('Anna', 'Nowak', 'annanowak@gmail.com');
INSERT INTO application_user(first_name, last_name, email) VALUES ('Jan', 'Kowalski', 'jankowalski@gmail.com');

INSERT INTO user_role(role, user_id) VALUES ('USER', 1);
INSERT INTO user_role(role, user_id) VALUES ('ADMIN', 1);
INSERT INTO user_role(role, user_id) VALUES ('USER', 2);

INSERT INTO shopping_list (name, user_id) VALUES ('Lista zakupowa 1', 1);
INSERT INTO shopping_list (name, user_id) VALUES ('Lista zakupowa 2', 2);
INSERT INTO shopping_list (name, user_id) VALUES ('Lista zakupowa 3', 1);

INSERT INTO category (name) VALUES ('Pieczywo');
INSERT INTO category (name) VALUES ('Ryba');
INSERT INTO category (name) VALUES ('Nabiał');
INSERT INTO category (name) VALUES ('Owoce');
INSERT INTO category (name) VALUES ('Warzywa');

INSERT INTO product (name, quantity, shopping_List_Id, category_id) VALUES ('Bułka', 4, 1, 1);
INSERT INTO product (name, quantity, shopping_List_Id, category_id) VALUES ('Chleb', 1, 3, 1);
INSERT INTO product (name, quantity, shopping_List_Id, category_id) VALUES ('Makrela', 1, 1, 2);
INSERT INTO product (name, quantity, shopping_List_Id, category_id) VALUES ('Dorsz', 1, 1, 2);
INSERT INTO product (name, quantity, shopping_List_Id, category_id) VALUES ('Pomidor', 4, 2, 5);
INSERT INTO product (name, quantity, shopping_List_Id, category_id) VALUES ('Banan', 4, 2, 4);
INSERT INTO product (name, quantity, shopping_List_Id, category_id) VALUES ('Jogurt', 2, 2, 3);