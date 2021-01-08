-- liquibase formatted sql
-- changeSet Pitulei:activating_users endDelimiter:; splitStatements:true


-- USERS
UPDATE users
SET activated = 1;