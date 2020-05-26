BEGIN^

CREATE EXTENSION IF NOT EXISTS pgcrypto^

INSERT INTO roles (id, authority) VALUES 
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER')^

INSERT INTO users (id, username, password) VALUES 
(1, 'admin', crypt('admin', gen_salt('bf'))),
(2, 'user', crypt('user', gen_salt('bf')))^

INSERT INTO users_roles (user_id, role_id) VALUES 
(1, 1),
(1, 2),
(2, 2)^

INSERT INTO categories (name) VALUES 
('food')^

INSERT INTO products (name, price, category_id) VALUES 
('apple', 0.5, 1),
('banana', 0.4, 1),
('tomato', 0.3, 1),
('pear', 0.35, 1),
('beer', 1.25, 1),
('bread', 1, 1),
('potato', 0.26, 1)^

COMMIT^