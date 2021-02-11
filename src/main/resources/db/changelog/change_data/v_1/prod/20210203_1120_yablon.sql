-- liquibase formatted sql
-- changeSet Yablon:table for expirationDate endDelimiter:; splitStatements:true

CREATE TABLE if not exists `material_expirations` (
    `id` int NOT NULL AUTO_INCREMENT,
    `material_id` int NOT NULL,
    `group_id` int NOT NULL,
    `start_date` datetime DEFAULT NULL,
    `expiration_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_expiration_group_idx` (`group_id`),
    KEY `fk_expiration_material_idx` (`material_id`),
    CONSTRAINT `fk_expiration_group` FOREIGN KEY (`group_id`) REFERENCES `student_groups` (`id`),
    CONSTRAINT `fk_expiration_material` FOREIGN KEY (`material_id`) REFERENCES `material` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

