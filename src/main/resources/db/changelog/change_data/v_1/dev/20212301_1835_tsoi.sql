-- liquibase formatted sql
-- changeSet tsoi:avatar add endDelimiter:; splitStatements:true

ALTER TABLE users
    ADD COLUMN avatar blob DEFAULT NULL;