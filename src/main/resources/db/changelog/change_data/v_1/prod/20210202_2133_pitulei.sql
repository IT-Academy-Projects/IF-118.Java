-- liquibase formatted sql
-- changeSet Pitulei:rename_picked_role endDelimiter:; splitStatements:false

-- USERS
ALTER TABLE users
    RENAME COLUMN is_picked_role TO picked_role;