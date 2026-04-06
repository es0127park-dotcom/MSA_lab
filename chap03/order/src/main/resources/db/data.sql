INSERT INTO order_tb (user_id,  status, created_at, updated_at) VALUES (1, 'COMPLETED', now(), now()); 
INSERT INTO order_tb (user_id,  status, created_at, updated_at) VALUES (2, 'CANCELLED', now(), now()); 
INSERT INTO order_tb (user_id,  status, created_at, updated_at) VALUES (3, 'PENDING', now(), now());

INSERT INTO order_item_tb (order_id, product_id, quantity, price, created_at, updated_at) VALUES (1, 1, 1, 2500000, now(), now());
INSERT INTO order_item_tb (order_id, product_id, quantity, price, created_at, updated_at) VALUES (2, 3, 1, 300000, now(), now());
INSERT INTO order_item_tb (order_id, product_id, quantity, price, created_at, updated_at) VALUES (3, 2, 2, 1300000, now(), now());