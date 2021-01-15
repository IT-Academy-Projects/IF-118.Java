-- liquibase formatted sql
-- changeSet Pitulei:role_pick_for_test_users endDelimiter:; splitStatements:true


-- USERS
UPDATE users
SET is_picked_role = 1;