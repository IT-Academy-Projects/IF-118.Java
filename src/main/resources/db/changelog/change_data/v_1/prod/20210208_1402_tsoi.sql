-- liquibase formatted sql
-- changeSet tsoi:create_user_report_table endDelimiter:; splitStatements:true

CREATE TABLE IF NOT EXISTS `user_report` (
    `user_id` INT NOT NULL,
    `group_id` INT NOT NULL,
    `assignments` JSON NOT NULL,
    `avg` VARCHAR(6) DEFAULT NULL,
    `updatable` BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (user_id, group_id)
);