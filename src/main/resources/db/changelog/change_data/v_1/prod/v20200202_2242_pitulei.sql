-- liquibase formatted sql
-- changeSet pitulei:password_reset_used endDelimiter:; splitStatements:true

ALTER TABLE password_reset_token
    ADD COLUMN used boolean DEFAULT NULL,
    ADD CONSTRAINT token_unique UNIQUE (token);