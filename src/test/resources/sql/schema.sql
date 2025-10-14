-- ==============================
-- USERS (MappedSuperclass)
-- ==============================
    CREATE TABLE IF NOT EXISTS clients (
    `user_id` bigint NOT NULL AUTO_INCREMENT,
    `email` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `name` varchar(255) NOT NULL,
    `balance` decimal(15,2) DEFAULT '0.00',
    `role` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `email` (`email`)
    ) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

    CREATE TABLE IF NOT EXISTS employees (
    `user_id` bigint NOT NULL AUTO_INCREMENT,
    `email` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `name` varchar(255) NOT NULL,
    `phone` varchar(50) DEFAULT NULL,
    `birth_date` date DEFAULT NULL,
    `role` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `email` (`email`)
    ) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- ==============================
-- BOOKS
-- ==============================
    CREATE TABLE IF NOT EXISTS books (
    `book_id` bigint NOT NULL AUTO_INCREMENT,
    `book_id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `genre` varchar(100) DEFAULT NULL,
    `age_group` varchar(50) DEFAULT NULL,
    `price` decimal(15,2) DEFAULT NULL,
    `publication_year` date DEFAULT NULL,
    `author` varchar(255) DEFAULT NULL,
    `number_of_pages` int DEFAULT NULL,
    `characteristics` text,
    `description` text,
    `language` varchar(50) DEFAULT NULL,
    `image_url` varchar(300) DEFAULT NULL,
    PRIMARY KEY (`book_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- ==============================
-- ORDERS
-- ==============================
    CREATE TABLE IF NOT EXISTS orders (
        `order_id` bigint NOT NULL AUTO_INCREMENT,
        `client_id` bigint NOT NULL,
        `employee_id` bigint DEFAULT NULL,
        `order_date` timestamp NULL DEFAULT NULL,
        `price` decimal(15,2) DEFAULT NULL,
        `status` varchar(45) DEFAULT NULL,
        PRIMARY KEY (`order_id`),
        KEY `fk_orders_clients` (`client_id`),
        KEY `fk_orders_employees` (`employee_id`),
        CONSTRAINT `fk_orders_clients` FOREIGN KEY (`client_id`) REFERENCES `clients` (`user_id`) ON DELETE RESTRICT,
        CONSTRAINT `fk_orders_employees` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`user_id`) ON DELETE RESTRICT
    ) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- ==============================
-- BOOK ITEMS
-- ==============================
    CREATE TABLE IF NOT EXISTS book_items (
        `book_item_id` bigint NOT NULL AUTO_INCREMENT,
        `order_id` bigint DEFAULT NULL,
        `book_id` bigint DEFAULT NULL,
        `quantity` int NOT NULL,
        PRIMARY KEY (`book_item_id`),
        KEY `fk_bookitems_order` (`order_id`),
        KEY `fk_bookitems_book` (`book_id`),
        CONSTRAINT `fk_bookitems_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE,
        CONSTRAINT `fk_bookitems_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE
    ) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- ==============================
-- CARTS
-- ==============================
    CREATE TABLE IF NOT EXISTS carts (
        `cart_id` bigint NOT NULL AUTO_INCREMENT,
                                         `client_id` bigint NOT NULL,
                                         PRIMARY KEY (`cart_id`),
        KEY `fk_carts_clients` (`client_id`),
        CONSTRAINT `fk_carts_clients` FOREIGN KEY (`client_id`) REFERENCES `clients` (`user_id`) ON DELETE CASCADE
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- ==============================
-- CART ITEMS
-- ==============================
    CREATE TABLE IF NOT EXISTS cart_items (
        `cart_id` bigint NOT NULL AUTO_INCREMENT,
        `client_id` bigint NOT NULL,
        PRIMARY KEY (`cart_id`),
        KEY `fk_carts_clients` (`client_id`),
        CONSTRAINT `fk_carts_clients` FOREIGN KEY (`client_id`) REFERENCES `clients` (`user_id`) ON DELETE CASCADE
        )

        CONSTRAINT fk_cartitems_book FOREIGN KEY (book_id)
        REFERENCES books(book_id) ON DELETE CASCADE
    );
