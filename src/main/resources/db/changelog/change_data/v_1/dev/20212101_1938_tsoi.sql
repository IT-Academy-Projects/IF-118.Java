-- liquibase formatted sql
-- changeSet tsoi:invitationCode add endDelimiter:; splitStatements:true

ALTER TABLE users
ADD COLUMN invitation_code VARCHAR(256) DEFAULT NULL;