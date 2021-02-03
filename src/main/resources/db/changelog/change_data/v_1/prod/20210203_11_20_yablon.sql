-- liquibase formatted sql
-- changeSet Yablon:table for expirationDate endDelimiter:; splitStatements:true

CREATE TABLE `material_expirations` (
    `material_id` int NOT NULL,
    `group_id` int NOT NULL,
    `start_date` datetime DEFAULT NULL,
    `expiration_date` datetime DEFAULT NULL,
    `opened` tinyint(1) NOT NULL,
    PRIMARY KEY (`material_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;