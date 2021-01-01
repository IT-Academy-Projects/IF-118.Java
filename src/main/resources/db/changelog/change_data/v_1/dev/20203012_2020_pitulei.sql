-- liquibase formatted sql
-- changeSet Pitulei:basic_users_for_testing endDelimiter:; splitStatements:true


-- USERS
INSERT INTO users(id, name, password, email, created_at, updated_at)
VALUES (3, 'Admin', '$2a$10$TWmwkXgjHAUrhy0XAAu/6ejyl.dPhu7EnUZK4Dteo/HbXztd4Htdi', 'admin@example.com', '2020-12-23 13:40:09',
        '2020-12-23 13:40:09');

INSERT INTO users(id, name, password, email, created_at, updated_at)
VALUES (2, 'Student', '$2a$10$TWmwkXgjHAUrhy0XAAu/6ejyl.dPhu7EnUZK4Dteo/HbXztd4Htdi', 'student@example.com', '2020-12-23 13:40:09',
        '2020-12-23 13:40:09');

INSERT INTO users(id, name, password, email, created_at, updated_at)
VALUES (1, 'Teacher', '$2a$10$TWmwkXgjHAUrhy0XAAu/6ejyl.dPhu7EnUZK4Dteo/HbXztd4Htdi', 'teacher@example.com', '2020-12-23 12:40:09',
        '2020-12-23 12:40:09');

-- USERS_ROLES
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (1, 2),
       (2, 3),
       (3, 4);