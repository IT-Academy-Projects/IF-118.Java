-- liquibase formatted sql
-- changeSet tsoi:updatedAt_add endDelimiter:; splitStatements:true

ALTER TABLE invitation
ADD COLUMN updated_at datetime DEFAULT NULL;