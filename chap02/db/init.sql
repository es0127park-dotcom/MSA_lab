-- User 테이블 생성
CREATE TABLE user_tb (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(50),
  password VARCHAR(50),
  roles VARCHAR(50),
  created_at DATETIME,
  updated_at DATETIME
);

-- Product 테이블 생성
CREATE TABLE product_tb (
  id INT AUTO_INCREMENT PRIMARY KEY,
  product_name VARCHAR(50),
  quantity INT,
  price BIGINT,
  created_at DATETIME,
  updated_at DATETIME
);

-- Order 테이블 생성
CREATE TABLE order_tb (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  status VARCHAR(50),
  created_at DATETIME,
  updated_at DATETIME
);

-- OrderItem 테이블 생성
CREATE TABLE order_item_tb (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT,
  product_id INT,
  quantity INT,
  price BIGINT,
  created_at DATETIME,
  updated_at DATETIME
);

-- Delivery 테이블 생성
CREATE TABLE delivery_tb (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT,
  address VARCHAR(50),
  status VARCHAR(50),
  created_at DATETIME,
  updated_at DATETIME
);

-- User 더미 데이터
INSERT INTO user_tb (username, email, password, roles, created_at, updated_at) VALUES ('ssar','ssar@metacoding.com','1234','USER',now(),now());
INSERT INTO user_tb (username, email, password, roles, created_at, updated_at) VALUES ('cos','cos@metacoding.com','1234','USER',now(),now());
INSERT INTO user_tb (username, email, password, roles, created_at, updated_at) VALUES ('love','love@metacoding.com','1234','USER',now(),now());

-- Product 더미 데이터
INSERT INTO product_tb (product_name, quantity, price, created_at, updated_at) VALUES ('MacBook Pro', 10, 2500000, now(), now());
INSERT INTO product_tb (product_name, quantity, price, created_at, updated_at) VALUES ('iPhone 15', 0, 1300000, now(), now());
INSERT INTO product_tb (product_name, quantity, price, created_at, updated_at) VALUES ('AirPods', 10, 300000, now(), now());

-- Order 더미 데이터
INSERT INTO order_tb (user_id,  status, created_at, updated_at) VALUES (1, 'COMPLETED', now(), now()); 
INSERT INTO order_tb (user_id,  status, created_at, updated_at) VALUES (2, 'CANCELLED', now(), now()); 
INSERT INTO order_tb (user_id,  status, created_at, updated_at) VALUES (3, 'PENDING', now(), now());

-- OrderItem 더미 데이터
INSERT INTO order_item_tb (order_id, product_id, quantity, price, created_at, updated_at) VALUES (1, 1, 1, 2500000, now(), now());
INSERT INTO order_item_tb (order_id, product_id, quantity, price, created_at, updated_at) VALUES (2, 3, 1, 300000, now(), now());
INSERT INTO order_item_tb (order_id, product_id, quantity, price, created_at, updated_at) VALUES (3, 2, 2, 1300000, now(), now());

-- Delivery 더미 데이터
INSERT INTO delivery_tb (order_id, address, status, created_at, updated_at) VALUES (1, 'Addr 1', 'COMPLETED', NOW(), NOW());
INSERT INTO delivery_tb (order_id, address, status, created_at, updated_at) VALUES (2, 'Addr 2', 'COMPLETED', NOW(), NOW());
INSERT INTO delivery_tb (order_id, address, status, created_at, updated_at) VALUES (3, 'Addr 3', 'COMPLETED', NOW(), NOW());
