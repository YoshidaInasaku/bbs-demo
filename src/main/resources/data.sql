-- password = "general"
INSERT INTO users(user_id, user_name, password, email, existence)
    VALUES('tanaka', '田中一般', '$2a$10$6fPXYK.C9rCWUBifuqBIB.GRNU.nQtBpdzkkKis8ETaKVKxNo/ltO', 'tanaka@example.com', true);
-- password = "admin"
INSERT INTO users(user_id, user_name, password, email, existence)
    VALUES('morita', '森田管理', '$2a$10$SJTWvNl16fCU7DaXtWC0DeN/A8IOakpCkWWNZ/FKRV2CHvWElQwMS', 'morita@example.com', true);

INSERT INTO roles(id, role_name) VALUES(1, 'ROLE_GENERAL');
INSERT INTO roles(id, role_name) VALUES(2, 'ROLE_ADMIN');

INSERT INTO user_role(user_id, role_id) VALUES(1, 1);
INSERT INTO user_role(user_id, role_id) VALUES(2, 1);
INSERT INTO user_role(user_id, role_id) VALUES(2, 2);