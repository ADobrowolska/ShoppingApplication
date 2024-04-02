# ShoppingList Application
> ShoppingList is a web application created to facilitate planning and organizing shopping. It allows users to create shopping lists, edit, and manage them in a convenient and efficient manner.
 <!-- If you have the project hosted somewhere, include the link here. > Live demo [_here_](https://www.example.com). -->


## Table of Contents
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Screenshots](#screenshots)
* [Setup](#setup)
<!-- * [License](#license) -->


## Technologies Used
- Java 17 
- Maven 
- SpringBoot 2 (Spring Core, SpringData JPA)
- MySQL 8.0.31
- Liquibase
- Hibernate 
- Junit 5 
- MockMvc
- Lombok


## Features
- Creating Lists: Users can easily create new shopping lists, assigning them appropriate names, according to the current date.
- Adding Products: The application allows for quick addition of products to the shopping list. Users can input the product name, quantity, and category.
- Editing and Deleting: Users have the ability to edit or delete products from the list as needed. They can change the quantity or delete completely. Additionally, the application deletes shopping lists older than 14 days.
- Browsing and Sorting: The application enables easy browsing of shopping lists and sorting products by category.


## Screenshots
Database diagram:

![DB_diagram](https://github.com/ADobrowolska/ShoppingApplication/assets/146584571/fc71fc17-7e1a-4d4e-ae24-6f3a5ccb952a)
<!-- If you have screenshots you'd like to share, include them here. ![Example screenshot](./img/screenshot.png) -->


## Setup
In order to run this application you will need:
- Java 17 - Main Backend Language
- Maven - Dependency Management
- MySQL - The default RDBMS

Before starting the application, you will need to set up the database. By default, application attempts to connect to 
a database called shopping-list in localhost:3306. You can change the default location and name of the database in application.properties.



<!-- Optional -->
<!-- ## License -->
<!-- This project is open source and available under the [... License](). -->

<!-- You don't have to include all sections - just the one's relevant to your project -->
