-- liquibase formatted sql
-- changeSet Pitulei:default_roles_and_authorities endDelimiter:; splitStatements:true

--TODO bk using hardcoded ids here is not a good practice.
-- AUTHORITY
INSERT INTO authority (id, created_at, updated_at, name)
VALUES (1, now(), now(), 'user.update'),
       (2, now(), now(), 'user.delete'),
       (3, now(), now(), 'group.create'),
       (4, now(), now(), 'group.read'),
       (5, now(), now(), 'group.update'),
       (6, now(), now(), 'group.delete'),
       (7, now(), now(), 'course.create'),
       (8, now(), now(), 'course.read'),
       (9, now(), now(), 'course.update'),
       (10, now(), now(), 'course.delete'),
       (11, now(), now(), 'swagger');

-- ROLE
INSERT INTO role (id, created_at, updated_at, name)
VALUES (1, now(), now(), 'USER'),
       (2, now(), now(), 'ADMIN'),
       (3, now(), now(), 'STUDENT'),
       (4, now(), now(), 'TEACHER');

-- ROLE_AUTHORITIES
INSERT INTO roles_authorities (role_id, authority_id)
VALUES (1, 1),
       (2, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 3),
       (2, 4),
       (3, 4),
       (4, 4),
       (2, 5),
       (4, 5),
       (2, 6),
       (4, 6),
       (2, 7),
       (4, 7),
       (2, 8),
       (3, 8),
       (4, 8),
       (2, 9),
       (4, 9),
       (2, 10),
       (4, 10),
       (2, 11);