-- password = "general"
INSERT INTO users(user_id, user_name, password, email)
VALUES('tanaka', '田中一般', '$2a$10$6fPXYK.C9rCWUBifuqBIB.GRNU.nQtBpdzkkKis8ETaKVKxNo/ltO', 'tanaka@example.com');
-- password = "admin"
INSERT INTO users(user_id, user_name, password, email)
VALUES('morita', '森田管理', '$2a$10$SJTWvNl16fCU7DaXtWC0DeN/A8IOakpCkWWNZ/FKRV2CHvWElQwMS', 'morita@example.com');

INSERT INTO roles(id, role_name) VALUES(1, 'ROLE_GENERAL');
INSERT INTO roles(id, role_name) VALUES(2, 'ROLE_ADMIN');

INSERT INTO user_role(user_id, role_id) VALUES('tanaka', 1);
INSERT INTO user_role(user_id, role_id) VALUES('morita', 1);
INSERT INTO user_role(user_id, role_id) VALUES('morita', 2);

INSERT INTO contents(text_content, updated_at, user_id) VALUES('こんにちは', '2022年8月1日 17時3分', 'tanaka');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('接続確認', '2022年8月1日 17時2分', 'morita');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('こんにちは', '2022年8月1日 17時5分', 'morita');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('旅行に行きたい', '2022年8月1日 17時6分', 'tanaka');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('分かる', '2022年8月1日 17時7分', 'morita');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('どこに行きたいですか？', '2022年8月1日 17時8分', 'morita');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('パプアニューギニア', '2022年8月1日 17時9分', 'tanaka');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('分かる', '2022年8月1日 17時10分', 'morita');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('こんにちは', '2022年8月1日 17時11分', 'tanaka');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('こんにちは', '2022年8月1日 17時12分', 'morita');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('人がきませんね...', '2022年8月1日 17時13分', 'tanaka');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('そうですね...', '2022年8月1日 17時14分', 'morita');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('旅行に行きたい', '2022年8月1日 17時5分', 'morita');
INSERT INTO contents(text_content, updated_at, user_id) VALUES('分かる', '2022年8月1日 17時5分', 'tanaka');

