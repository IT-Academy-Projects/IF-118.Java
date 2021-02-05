-- liquibase formatted sql
-- changeSet yablon:dueDateTime_drop endDelimiter:; splitStatements:true

ALTER TABLE material
    drop COLUMN due_date_time;