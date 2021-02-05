-- liquibase formatted sql
-- changeSet yablon:dueDateTime_add endDelimiter:; splitStatements:true

ALTER TABLE material
    ADD COLUMN due_date_time datetime DEFAULT NULL;