-- liquibase formatted sql
-- changeSet Pitulei:fields_for_email_confirmation endDelimiter:; splitStatements:false

  -- USERS
   ALTER TABLE users
    ADD COLUMN activation_code VARCHAR(40) AFTER disabled,
    ADD COLUMN activated boolean AFTER activation_code;