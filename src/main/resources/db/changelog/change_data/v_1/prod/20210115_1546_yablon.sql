-- liquibase formatted sql
-- changeSet Yablon:default_roles_and_authorities endDelimiter:; splitStatements:true

INSERT INTO authority (id, created_at, updated_at, name)
VALUES (12, now(), now(), 'comment.read'),
       (13, now(), now(), 'comment.readPrivate'),
       (14, now(), now(), 'comment.readAllPrivate');

INSERT INTO roles_authorities (role_id, authority_id)
VALUES (3, 12),
       (3, 13),
       (4, 12),
       (4, 13),
       (4, 14)