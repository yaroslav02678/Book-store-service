-- ==============================
-- CLIENTS
-- ==============================
CREATE TABLE clients (
                         user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         balance DECIMAL(15,2) DEFAULT 0.00,
                         role VARCHAR(45) DEFAULT NULL
);

-- ==============================
-- EMPLOYEES
-- ==============================
CREATE TABLE employees (
                           user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           email VARCHAR(255) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL,
                           name VARCHAR(255) NOT NULL,
                           phone VARCHAR(50),
                           birth_date DATE,
                           role VARCHAR(45)
);

-- ==============================
-- BOOKS
-- ==============================
CREATE TABLE books (
                       book_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       genre VARCHAR(100),
                       age_group VARCHAR(50),
                       price DECIMAL(15,2),
                       publication_year DATE,
                       author VARCHAR(255),
                       number_of_pages INT,
                       characteristics TEXT,
                       description TEXT,
                       language VARCHAR(50),
                       image_url VARCHAR(300)
);

-- ==============================
-- ORDERS
-- ==============================
CREATE TABLE orders (
                        order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        client_id BIGINT NOT NULL,
                        employee_id BIGINT,
                        order_date TIMESTAMP,
                        price DECIMAL(15,2),
                        status VARCHAR(45),
                        CONSTRAINT fk_orders_clients FOREIGN KEY (client_id) REFERENCES clients(user_id) ON DELETE RESTRICT,
                        CONSTRAINT fk_orders_employees FOREIGN KEY (employee_id) REFERENCES employees(user_id) ON DELETE RESTRICT
);

-- ==============================
-- BOOK ITEMS
-- ==============================
CREATE TABLE book_items (
                            book_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            order_id BIGINT,
                            book_id BIGINT,
                            quantity INT NOT NULL,
                            CONSTRAINT fk_bookitems_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                            CONSTRAINT fk_bookitems_book FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
);

-- ==============================
-- CARTS
-- ==============================
CREATE TABLE carts (
                       cart_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       client_id BIGINT NOT NULL,
                       CONSTRAINT fk_carts_clients FOREIGN KEY (client_id) REFERENCES clients(user_id) ON DELETE CASCADE
);

-- ==============================
-- CART ITEMS
-- ==============================
CREATE TABLE cart_items (
                            cart_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            cart_id BIGINT NOT NULL,
                            book_id BIGINT NOT NULL,
                            quantity INT DEFAULT 1,
                            CONSTRAINT fk_cartitems_cart FOREIGN KEY (cart_id) REFERENCES carts(cart_id) ON DELETE CASCADE,
                            CONSTRAINT fk_cartitems_book FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
);
