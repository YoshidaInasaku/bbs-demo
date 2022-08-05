DROP TABLE IF EXISTS contents;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(15) NOT NULL UNIQUE,
    user_name VARCHAR(24) NOT NULL,
    password VARCHAR(128) NOT NULL,
    email VARCHAR(64) NOT NULL
);

CREATE TABLE roles(
    id INTEGER PRIMARY KEY,
    role_name VARCHAR(24)
);

-- usersとrolesの対応付け
CREATE TABLE user_role(
    user_id INTEGER,
    role_id INTEGER,
    CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE contents(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    text_content VARCHAR(140) NOT NULL,
    updated_at VARCHAR(64),
    user_id VARCHAR(15)
);