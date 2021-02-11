-- liquibase formatted sql
-- changeSet Pitulei:fields_for_google_role_pick endDelimiter:; splitStatements:false

  -- USERS
   ALTER TABLE users
    ADD COLUMN is_picked_role boolean AFTER activated;