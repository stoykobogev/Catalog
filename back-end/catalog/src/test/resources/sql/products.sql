INSERT INTO products (id, name, price, category_id, image_code) VALUES 
(1, 'apple', 0.5, 1, RAWTOHEX(HASH('SHA256', STRINGTOUTF8('a')))),
(2, 'banana', 0.4, 1, RAWTOHEX(HASH('SHA256', STRINGTOUTF8('b')))),
(3, 'sweater', 2, 2, RAWTOHEX(HASH('SHA256', STRINGTOUTF8('a'))));