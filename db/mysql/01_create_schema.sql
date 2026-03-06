-- =====================================================
-- Shopping Cart API - MySQL schema (MySQL 8+)
-- =====================================================

CREATE DATABASE IF NOT EXISTS shopping_cart_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE shopping_cart_db;

-- -----------------------------------------------------
-- app_users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS app_users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_app_users_username (username)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- clients
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS clients (
  id BIGINT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(180) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_clients_email (email),
  KEY idx_clients_last_name (last_name)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- orders
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  client_id BIGINT NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_orders_client_status (client_id, status),
  KEY idx_orders_created_at (created_at),
  CONSTRAINT fk_orders_client FOREIGN KEY (client_id)
    REFERENCES clients(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- order_details
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS order_details (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  product_title VARCHAR(255) NOT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_order_details_order (order_id),
  KEY idx_order_details_product (product_id),
  CONSTRAINT fk_order_details_order FOREIGN KEY (order_id)
    REFERENCES orders(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  CONSTRAINT ck_order_details_quantity CHECK (quantity > 0),
  CONSTRAINT ck_order_details_unit_price CHECK (unit_price >= 0)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- payments
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS payments (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  payer_names VARCHAR(120) NULL,
  payer_surnames VARCHAR(120) NULL,
  payer_email VARCHAR(180) NULL,
  payer_phone VARCHAR(40) NULL,
  processed_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_payments_order (order_id),
  KEY idx_payments_processed_at (processed_at),
  KEY idx_payments_status (status),
  CONSTRAINT fk_payments_order FOREIGN KEY (order_id)
    REFERENCES orders(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT ck_payments_amount CHECK (amount > 0)
) ENGINE=InnoDB;
