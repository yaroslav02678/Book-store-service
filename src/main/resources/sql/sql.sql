-- Відключаємо перевірку зовнішніх ключів для безпечного очищення таблиць
SET FOREIGN_KEY_CHECKS = 0;

-- Очищення таблиць у зворотному порядку залежностей
TRUNCATE TABLE `book_items`;
TRUNCATE TABLE `cart_items`;
TRUNCATE TABLE `orders`;
TRUNCATE TABLE `carts`;
TRUNCATE TABLE `books`;
TRUNCATE TABLE `clients`;
TRUNCATE TABLE `employees`;

-- Включаємо перевірку зовнішніх ключів
SET FOREIGN_KEY_CHECKS = 1;

-- ==============================
-- EMPLOYEES
-- ==============================
INSERT INTO employees (user_id, birth_date, email, name, password, phone, role)
VALUES (1, '1990-05-15', 'john.doe@email.com', 'John Doe', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-123-4567', 'ROLE_EMPLOYEE'),
       (2, '1985-09-20', 'jane.smith@email.com', 'Jane Smith', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-987-6543', 'ROLE_EMPLOYEE'),
       (3, '1978-03-08', 'bob.jones@email.com', 'Bob Jones', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-321-6789', 'ROLE_EMPLOYEE'),
       (4, '1982-11-25', 'alice.white@email.com', 'Alice White', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-876-5432', 'ROLE_EMPLOYEE'),
       (5, '1995-07-12', 'mike.wilson@email.com', 'Mike Wilson', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-234-5678', 'ROLE_EMPLOYEE'),
       (6, '1989-01-30', 'sara.brown@email.com', 'Sara Brown', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-876-5433', 'ROLE_EMPLOYEE'),
       (7, '1975-06-18', 'tom.jenkins@email.com', 'Tom Jenkins', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-345-6789', 'ROLE_EMPLOYEE'),
       (8, '1987-12-04', 'lisa.taylor@email.com', 'Lisa Taylor', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-789-0123', 'ROLE_EMPLOYEE'),
       (9, '1992-08-22', 'david.wright@email.com', 'David Wright', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-456-7890', 'ROLE_EMPLOYEE'),
       (10, '1980-04-10', 'emily.harris@email.com', 'Emily Harris', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', '555-098-7654', 'ROLE_EMPLOYEE'),
       (11, '1970-04-12', 'admin@email.com', 'Admin', '$2a$12$iTGkFy5awPFJgm7vWQUqE.tgk08KK0g.aqptkkSFTWAp02E/GvJy', '555-094-2424', 'ROLE_ADMIN');

-- ==============================
-- CLIENTS
-- ==============================
INSERT INTO clients (user_id, balance, email, name, password, role)
VALUES (1, 1000.00, 'client1@example.com', 'Medelyn Wright', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
       (2, 1500.50, 'client2@example.com', 'Landon Phillips', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
       (3, 800.75, 'client3@example.com', 'Harmony Mason', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
       (4, 1200.25, 'client4@example.com', 'Archer Harper', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
       (5, 900.80, 'client5@example.com', 'Kira Jacobs', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
       (6, 1100.60, 'client6@example.com', 'Maximus Kelly', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
       (7, 1300.45, 'client7@example.com', 'Sierra Mitchell', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
       (8, 950.30, 'client8@example.com', 'Quinton Saunders', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
       (9, 1050.90, 'client9@example.com', 'Amina Clarke', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT'),
       (10, 880.20, 'client10@example.com', 'Bryson Chavez', '$2a$12$RDkHuqtbZtwWPNYHX4jbr.nGTcDw4jYbJuGyMKqhuCRd2PRE9Eaqi', 'ROLE_CLIENT');

-- ==============================
-- BOOKS
-- ==============================
INSERT INTO books (book_id, name, genre, age_group, price, publication_year, author, number_of_pages, characteristics, description, language, image_url)
VALUES (1, 'The Hidden Treasure', 'Adventure', 'ADULT', 24.99, '2018-05-15', 'Emily White', 400, 'Mysterious journey','An enthralling adventure of discovery', 'ENGLISH', 'https://example.com/image1.jpg'),
       (2, 'Echoes of Eternity', 'Fantasy', 'TEEN', 16.50, '2011-01-15', 'Daniel Black', 350, 'Magical realms', 'A spellbinding tale of magic and destiny', 'ENGLISH', 'https://example.com/image2.jpg'),
       (3, 'Whispers in the Shadows', 'Mystery', 'ADULT', 29.95, '2018-08-11', 'Sophia Green', 450, 'Intriguing suspense','A gripping mystery that keeps you guessing', 'ENGLISH', 'https://example.com/image3.jpg'),
       (4, 'The Starlight Sonata', 'Romance', 'ADULT', 21.75, '2011-05-15', 'Michael Rose', 320, 'Heartwarming love story','A beautiful journey of love and passion', 'ENGLISH', 'https://example.com/image4.jpg'),
       (5, 'Beyond the Horizon', 'Science Fiction', 'CHILD', 18.99, '2004-05-15', 'Alex Carter', 280,'Interstellar adventure', 'An epic sci-fi adventure beyond the stars', 'ENGLISH', 'https://example.com/image5.jpg'),
       (6, 'Dancing with Shadows', 'Thriller', 'ADULT', 26.50, '2015-05-15', 'Olivia Smith', 380, 'Suspenseful twists','A thrilling tale of danger and intrigue', 'ENGLISH', 'https://example.com/image6.jpg'),
       (7, 'Voices in the Wind', 'Historical Fiction', 'ADULT', 32.00, '2017-05-15', 'William Turner', 500,'Rich historical setting', 'A compelling journey through time', 'ENGLISH', 'https://example.com/image7.jpg'),
       (8, 'Serenade of Souls', 'Fantasy', 'TEEN', 15.99, '2013-05-15', 'Isabella Reed', 330, 'Enchanting realms','A magical fantasy filled with wonder', 'ENGLISH', 'https://example.com/image8.jpg'),
       (9, 'Silent Whispers', 'Mystery', 'ADULT', 27.50, '2021-05-15', 'Benjamin Hall', 420, 'Intricate detective work','A mystery that keeps you on the edge', 'ENGLISH', 'https://example.com/image9.jpg'),
       (10, 'Whirlwind Romance', 'Romance', 'OTHER', 23.25, '2022-05-15', 'Emma Turner', 360, 'Passionate love affair','A romance that sweeps you off your feet', 'ENGLISH', 'https://example.com/image10.jpg');

-- ==============================
-- CARTS
-- ==============================
-- Сценарій 1: Кошик з декількома товарами для client_id=1
INSERT INTO carts (cart_id, client_id) VALUES (1, 1);
-- Сценарій 2: Кошик з одним товаром для client_id=2
INSERT INTO carts (cart_id, client_id) VALUES (2, 2);
-- Сценарій 3: Порожній кошик для client_id=3
INSERT INTO carts (cart_id, client_id) VALUES (3, 3);
-- Примітка: client_id=4 не має кошика для тестування створення нового.

-- ==============================
-- CART ITEMS
-- ==============================
-- Додаємо 2 товари в кошик client_id=1 (cart_id=1)
INSERT INTO cart_items (cart_item_id, cart_id, book_id, quantity) VALUES (1, 1, 1, 2); -- 2 копії книги "The Hidden Treasure"
INSERT INTO cart_items (cart_item_id, cart_id, book_id, quantity) VALUES (2, 1, 3, 1); -- 1 копія книги "Whispers in the Shadows"

-- Додаємо 1 товар в кошик client_id=2 (cart_id=2)
INSERT INTO cart_items (cart_item_id, cart_id, book_id, quantity) VALUES (3, 2, 5, 1); -- 1 копія "Beyond the Horizon"

-- ==============================
-- ORDERS
-- ==============================
-- Сценарій 1: Нове замовлення для client_id=5, без працівника, статус NEW
INSERT INTO orders (order_id, client_id, employee_id, order_date, price, status)
VALUES (1, 5, NULL, '2025-10-10 10:00:00', 38.25, 'NEW');

-- Сценарій 2: Виконуване замовлення для client_id=6, призначене employee_id=2
INSERT INTO orders (order_id, client_id, employee_id, order_date, price, status)
VALUES (2, 6, 2, '2025-10-09 15:30:00', 64.00, 'IN_PROGRESS');

-- Сценарій 3: Ще одне (доставлене) замовлення для client_id=5 для тестування вибірки
INSERT INTO orders (order_id, client_id, employee_id, order_date, price, status)
VALUES (3, 5, 3, '2025-09-20 12:00:00', 24.99, 'DELIVERED');

-- Сценарій 4: Замовлення для client_id=7, призначене employee_id=3 для тестування вибірки
INSERT INTO orders (order_id, client_id, employee_id, order_date, price, status)
VALUES (4, 7, 3, '2025-10-11 11:00:00', 82.50, 'NEW');

-- ==============================
-- BOOK ITEMS (зв'язок замовлень з книгами)
-- ==============================
-- Позиції для order_id=1
INSERT INTO book_items (book_item_id, order_id, book_id, quantity) VALUES (1, 1, 2, 1); -- 16.50
INSERT INTO book_items (book_item_id, order_id, book_id, quantity) VALUES (2, 1, 4, 1); -- 21.75
-- Total: 38.25

-- Позиції для order_id=2
INSERT INTO book_items (book_item_id, order_id, book_id, quantity) VALUES (3, 2, 7, 2); -- 32.00 * 2 = 64.00
-- Total: 64.00

-- Позиції для order_id=3
INSERT INTO book_items (book_item_id, order_id, book_id, quantity) VALUES (4, 3, 1, 1); -- 24.99
-- Total: 24.99

-- Позиції для order_id=4
INSERT INTO book_items (book_item_id, order_id, book_id, quantity) VALUES (5, 4, 9, 3); -- 27.50 * 3 = 82.50
-- Total: 82.50