-- INSERT INTO employees (birth_date, email, name, password, phone, role)
-- VALUES ('1990-05-15', 'john.doe@email.com', 'John Doe', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-123-4567', 'ROLE_EMPLOYEE'),
--        ('1985-09-20', 'jane.smith@email.com', 'Jane Smith', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-987-6543', 'ROLE_EMPLOYEE'),
--        ('1978-03-08', 'bob.jones@email.com', 'Bob Jones', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-321-6789', 'ROLE_EMPLOYEE'),
--        ('1982-11-25', 'alice.white@email.com', 'Alice White', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-876-5432', 'ROLE_EMPLOYEE'),
--        ('1995-07-12', 'mike.wilson@email.com', 'Mike Wilson', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-234-5678', 'ROLE_EMPLOYEE'),
--        ('1989-01-30', 'sara.brown@email.com', 'Sara Brown', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-876-5433', 'ROLE_EMPLOYEE'),
--        ('1975-06-18', 'tom.jenkins@email.com', 'Tom Jenkins', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-345-6789', 'ROLE_EMPLOYEE'),
--        ('1987-12-04', 'lisa.taylor@email.com', 'Lisa Taylor', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-789-0123', 'ROLE_EMPLOYEE'),
--        ('1992-08-22', 'david.wright@email.com', 'David Wright', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-456-7890', 'ROLE_EMPLOYEE'),
--        ('1980-04-10', 'emily.harris@email.com', 'Emily Harris', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqid', '555-098-7654', 'ROLE_EMPLOYEE'),
--        ('1970-04-12', 'admin@email.com', 'Admin', '$2a$12$iTGkFy5awPFJgm7vWQUqE.tgk08KK0g.aqptkkSFTWAp02E/GvJy', '555-094-2424', 'ROLE_ADMIN');
--
--
-- INSERT INTO clients (balance, email, name, password, role)
-- VALUES (1000.00, 'client1@example.com', 'Medelyn Wright', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
--        (1500.50, 'client2@example.com', 'Landon Phillips', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqis', 'ROLE_CLIENT'),
--        (800.75, 'client3@example.com', 'Harmony Mason', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
--        (1200.25, 'client4@example.com', 'Archer Harper', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
--        (900.80, 'client5@example.com', 'Kira Jacobs', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
--        (1100.60, 'client6@example.com', 'Maximus Kelly', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
--        (1300.45, 'client7@example.com', 'Sierra Mitchell', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
--        (950.30, 'client8@example.com', 'Quinton Saunders', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
--        (1050.90, 'client9@example.com', 'Amina Clarke', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
--        (880.20, 'client10@example.com', 'Bryson Chavez', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT');
--
-- INSERT INTO books (name, genre, age_group, price, publication_year, author, number_of_pages, characteristics, description, language, image_url)
-- VALUES ('The Hidden Treasure', 'Adventure', 'ADULT', 24.99, '2018-05-15', 'Emily White', 400, 'Mysterious journey','An enthralling adventure of discovery', 'ENGLISH', 'https://res.cloudinary.com/hdtsjhzsw/image/upload/s--VMABrSer--/q_auto/c_fit,w_505,h_518/1897b2a9da6540c9338a5821f986b5d0e995cea4.jpg'),
--        ('Echoes of Eternity', 'Fantasy', 'TEEN', 16.50, '2011-01-15', 'Daniel Black', 350, 'Magical realms', 'A spellbinding tale of magic and destiny', 'ENGLISH', 'https://res.cloudinary.com/hdtsjhzsw/image/upload/s--VMABrSer--/q_auto/c_fit,w_505,h_518/1897b2a9da6540c9338a5821f986b5d0e995cea4.jpg'),
--        ('Whispers in the Shadows', 'Mystery', 'ADULT', 29.95, '2018-08-11', 'Sophia Green', 450, 'Intriguing suspense','A gripping mystery that keeps you guessing', 'ENGLISH', 'https://res.cloudinary.com/hdtsjhzsw/image/upload/s--VMABrSer--/q_auto/c_fit,w_505,h_518/1897b2a9da6540c9338a5821f986b5d0e995cea4.jpg'),
--        ('The Starlight Sonata', 'Romance', 'ADULT', 21.75, '2011-05-15', 'Michael Rose', 320, 'Heartwarming love story','A beautiful journey of love and passion', 'ENGLISH', 'https://res.cloudinary.com/hdtsjhzsw/image/upload/s--VMABrSer--/q_auto/c_fit,w_505,h_518/1897b2a9da6540c9338a5821f986b5d0e995cea4.jpg'),
--        ('Beyond the Horizon', 'Science Fiction', 'CHILD', 18.99, '2004-05-15', 'Alex Carter', 280,'Interstellar adventure', 'An epic sci-fi adventure beyond the stars', 'ENGLISH', 'https://res.cloudinary.com/hdtsjhzsw/image/upload/s--VMABrSer--/q_auto/c_fit,w_505,h_518/1897b2a9da6540c9338a5821f986b5d0e995cea4.jpg'),
--        ('Dancing with Shadows', 'Thriller', 'ADULT', 26.50, '2015-05-15', 'Olivia Smith', 380, 'Suspenseful twists','A thrilling tale of danger and intrigue', 'ENGLISH', 'https://res.cloudinary.com/hdtsjhzsw/image/upload/s--VMABrSer--/q_auto/c_fit,w_505,h_518/1897b2a9da6540c9338a5821f986b5d0e995cea4.jpg'),
--        ('Voices in the Wind', 'Historical Fiction', 'ADULT', 32.00, '2017-05-15', 'William Turner', 500,'Rich historical setting', 'A compelling journey through time', 'ENGLISH', 'https://res.cloudinary.com/hdtsjhzsw/image/upload/s--VMABrSer--/q_auto/c_fit,w_505,h_518/1897b2a9da6540c9338a5821f986b5d0e995cea4.jpg'),
--        ('Serenade of Souls', 'Fantasy', 'TEEN', 15.99, '2013-05-15', 'Isabella Reed', 330, 'Enchanting realms','A magical fantasy filled with wonder', 'ENGLISH', 'https://res.cloudinary.com/hdtsjhzsw/image/upload/s--VMABrSer--/q_auto/c_fit,w_505,h_518/1897b2a9da6540c9338a5821f986b5d0e995cea4.jpg'),
--        ('Silent Whispers', 'Mystery', 'ADULT', 27.50, '2021-05-15', 'Benjamin Hall', 420, 'Intricate detective work','A mystery that keeps you on the edge', 'ENGLISH', 'https://res.cloudinary.com/hdtsjhzsw/image/upload/s--VMABrSer--/q_auto/c_fit,w_505,h_518/1897b2a9da6540c9338a5821f986b5d0e995cea4.jpg'),
--        ('Whirlwind Romance', 'Romance', 'OTHER', 23.25, '2022-05-15', 'Emma Turner', 360, 'Passionate love affair','A romance that sweeps you off your feet', 'ENGLISH', 'https://res.cloudinary.com/hdtsjhzsw/image/upload/s--VMABrSer--/q_auto/c_fit,w_505,h_518/1897b2a9da6540c9338a5821f986b5d0e995cea4.jpg');

-- =================================================================
-- ==     TEST DATA INITIALIZATION SCRIPT (FOR H2 DATABASE)       ==
-- =================================================================
-- Версія 4: На основі даних, наданих користувачем, з явними ID.

-- Крок 1: Очищення таблиць у правильному порядку
DELETE FROM book_items;
DELETE FROM cart_items;
DELETE FROM orders;
DELETE FROM carts;
DELETE FROM books;
DELETE FROM clients;
DELETE FROM employees;

-- Крок 2: Наповнення таблиць тестовими даними

-- Співробітники (11 записів)
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role) VALUES (1, '1990-05-15', 'john.doe@email.com', 'John Doe', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-123-4567', 'ROLE_EMPLOYEE');
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role) VALUES (2, '1985-09-20', 'jane.smith@email.com', 'Jane Smith', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-987-6543', 'ROLE_EMPLOYEE');
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role) VALUES (3, '1978-03-08', 'bob.jones@email.com', 'Bob Jones', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-321-6789', 'ROLE_EMPLOYEE');
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role) VALUES (4, '1982-11-25', 'alice.white@email.com', 'Alice White', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-876-5432', 'ROLE_EMPLOYEE');
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role) VALUES (5, '1995-07-12', 'mike.wilson@email.com', 'Mike Wilson', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-234-5678', 'ROLE_EMPLOYEE');
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role) VALUES (6, '1989-01-30', 'sara.brown@email.com', 'Sara Brown', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-876-5433', 'ROLE_EMPLOYEE');
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role) VALUES (7, '1975-06-18', 'tom.jenkins@email.com', 'Tom Jenkins', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-345-6789', 'ROLE_EMPLOYEE');
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role) VALUES (8, '1987-12-04', 'lisa.taylor@email.com', 'Lisa Taylor', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-789-0123', 'ROLE_EMPLOYEE');
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role) VALUES (9, '1992-08-22', 'david.wright@email.com', 'David Wright', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-456-7890', 'ROLE_EMPLOYEE');
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role) VALUES (10, '1980-04-10', 'emily.harris@email.com', 'Emily Harris', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqid', '555-098-7654', 'ROLE_EMPLOYEE');
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role) VALUES (11, '1970-04-12', 'admin@email.com', 'Admin', '$2a$12$iTGkFy5awPFJgm7vWQUqE.tgk08KK0g.aqptkkSFTWAp02E/GvJy', '555-094-2424', 'ROLE_ADMIN');

-- Клієнти (10 записів)
INSERT INTO clients (user_id, balance, email, name, password, role) VALUES (1, 1000.00, 'client1@example.com', 'Medelyn Wright', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT');
INSERT INTO clients (user_id, balance, email, name, password, role) VALUES (2, 1500.50, 'client2@example.com', 'Landon Phillips', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqis', 'ROLE_CLIENT');
INSERT INTO clients (user_id, balance, email, name, password, role) VALUES (3, 800.75, 'client3@example.com', 'Harmony Mason', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT');
INSERT INTO clients (user_id, balance, email, name, password, role) VALUES (4, 1200.25, 'client4@example.com', 'Archer Harper', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT');
INSERT INTO clients (user_id, balance, email, name, password, role) VALUES (5, 900.80, 'client5@example.com', 'Kira Jacobs', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT');
INSERT INTO clients (user_id, balance, email, name, password, role) VALUES (6, 1100.60, 'client6@example.com', 'Maximus Kelly', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT');
INSERT INTO clients (user_id, balance, email, name, password, role) VALUES (7, 1300.45, 'client7@example.com', 'Sierra Mitchell', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT');
INSERT INTO clients (user_id, balance, email, name, password, role) VALUES (8, 950.30, 'client8@example.com', 'Quinton Saunders', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT');
INSERT INTO clients (user_id, balance, email, name, password, role) VALUES (9, 1050.90, 'client9@example.com', 'Amina Clarke', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT');
INSERT INTO clients (user_id, balance, email, name, password, role) VALUES (10, 880.20, 'client10@example.com', 'Bryson Chavez', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT');

-- Книги (10 записів)
INSERT INTO books (book_id, name, genre, age_group, price, publication_year, author, number_of_pages) VALUES (1, 'The Hidden Treasure', 'Adventure', 'ADULT', 24.99, '2018-05-15', 'Emily White', 400);
INSERT INTO books (book_id, name, genre, age_group, price, publication_year, author, number_of_pages) VALUES (2, 'Echoes of Eternity', 'Fantasy', 'TEEN', 16.50, '2011-01-15', 'Daniel Black', 350);
INSERT INTO books (book_id, name, genre, age_group, price, publication_year, author, number_of_pages) VALUES (3, 'Whispers in the Shadows', 'Mystery', 'ADULT', 29.95, '2018-08-11', 'Sophia Green', 450);
INSERT INTO books (book_id, name, genre, age_group, price, publication_year, author, number_of_pages) VALUES (4, 'The Starlight Sonata', 'Romance', 'ADULT', 21.75, '2011-05-15', 'Michael Rose', 320);
INSERT INTO books (book_id, name, genre, age_group, price, publication_year, author, number_of_pages) VALUES (5, 'Beyond the Horizon', 'Science Fiction', 'CHILD', 18.99, '2004-05-15', 'Alex Carter', 280);
INSERT INTO books (book_id, name, genre, age_group, price, publication_year, author, number_of_pages) VALUES (6, 'Dancing with Shadows', 'Thriller', 'ADULT', 26.50, '2015-05-15', 'Olivia Smith', 380);
INSERT INTO books (book_id, name, genre, age_group, price, publication_year, author, number_of_pages) VALUES (7, 'Voices in the Wind', 'Historical Fiction', 'ADULT', 32.00, '2017-05-15', 'William Turner', 500);
INSERT INTO books (book_id, name, genre, age_group, price, publication_year, author, number_of_pages) VALUES (8, 'Serenade of Souls', 'Fantasy', 'TEEN', 15.99, '2013-05-15', 'Isabella Reed', 330);
INSERT INTO books (book_id, name, genre, age_group, price, publication_year, author, number_of_pages) VALUES (9, 'Silent Whispers', 'Mystery', 'ADULT', 27.50, '2021-05-15', 'Benjamin Hall', 420);
INSERT INTO books (book_id, name, genre, age_group, price, publication_year, author, number_of_pages) VALUES (10, 'Whirlwind Romance', 'Romance', 'OTHER', 23.25, '2022-05-15', 'Emma Turner', 360);

-- Кошики та їх вміст
INSERT INTO carts (cart_id, client_id) VALUES (1, 1); -- Кошик для клієнта Medelyn Wright
INSERT INTO carts (cart_id, client_id) VALUES (2, 2); -- Кошик для клієнта Landon Phillips
INSERT INTO cart_items (cart_item_id, cart_id, book_id, quantity) VALUES (1, 1, 1, 2); -- 2 x 'The Hidden Treasure'
INSERT INTO cart_items (cart_item_id, cart_id, book_id, quantity) VALUES (2, 1, 3, 1); -- 1 x 'Whispers in the Shadows'
INSERT INTO cart_items (cart_item_id, cart_id, book_id, quantity) VALUES (3, 2, 5, 1); -- 1 x 'Beyond the Horizon'

-- Замовлення та їх вміст
INSERT INTO orders (order_id, client_id, employee_id, status, order_date, price) VALUES (1, 1, 11, 'NEW', '2025-10-01 10:00:00', 49.98);
INSERT INTO orders (order_id, client_id, employee_id, status, order_date, price) VALUES (2, 2, 1, 'COMPLETED', '2025-10-02 12:00:00', 16.50);
INSERT INTO orders (order_id, client_id, employee_id, status, order_date, price) VALUES (3, 1, NULL, 'NEW', '2025-10-03 14:00:00', 29.95);

INSERT INTO book_items (book_item_id, order_id, book_id, quantity) VALUES (1, 1, 1, 2);
INSERT INTO book_items (book_item_id, order_id, book_id, quantity) VALUES (2, 2, 2, 1);
INSERT INTO book_items (book_item_id, order_id, book_id, quantity) VALUES (3, 3, 3, 1);