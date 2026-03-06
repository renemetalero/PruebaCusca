USE shopping_cart_db;

-- password: admin123 (BCrypt)
INSERT INTO app_users (username, password, role)
VALUES ('admin', '$2a$10$9mhm4bExMPfCn7WQz6vG4ef5iqfAu0Jx4NQJ2CBxBdUmpVce7vgCG', 'ROLE_ADMIN')
ON DUPLICATE KEY UPDATE username = VALUES(username);

INSERT INTO clients (first_name, last_name, email)
VALUES ('Compat', 'Customer', 'compat.customer@local')
ON DUPLICATE KEY UPDATE first_name = VALUES(first_name), last_name = VALUES(last_name);
