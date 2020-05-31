# Catalog
A project to showcase some technologies. Web design is poorly made as I'm not profitient in it.


## About the application
It's a simple Single-page application with REST API. You can create products and place them in categories. Products have name, price and optional image. There are two users (user management is not the goal). The 'admin' can do all CRUD operations while the 'user' can only view the content.

## More details
* Uploading images is costly because of their big size. There is implemented functionality to check if the same image is already in the database and use it instead of the uploaded one hence there are no image duplications. The goal is to save disk memory.

* Security - JWT authentication and authorization complemented by Redis, CORS and CSRF protection, HTTP request parameters validations

* Extensive unit and integration testing

### Back-End tech stack
Java 8, Spring Boot (Spring Web, Spring Data, Spring Security), Hibernate, Redis, JWT, Maven, PostgreSQL, H2 Database, JUnit, Mockito, PowerMock etc.


### Front-End tech stack
Angular, Bootstrap, Fontawesome, jQuery (required by Bootstrap)
