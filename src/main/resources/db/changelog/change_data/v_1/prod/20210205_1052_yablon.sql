-- liquibase formatted sql
-- changeSet Yablon:table for groups-materials endDelimiter:; splitStatements:true

CREATE TABLE if not exists `groups_materials` (
    `group_id` int NOT NULL,
    `material_id` int NOT NULL,
    `opened` tinyint(1) DEFAULT '0',
    PRIMARY KEY (`group_id`,`material_id`),
    KEY `fk__groups__materials__group_id_idx` (`group_id`),
    KEY `fk__materials__groups__material_id_idx` (`material_id`),
    CONSTRAINT `fk__groups__materials__group_id` FOREIGN KEY (`group_id`) REFERENCES `student_groups` (`id`),
    CONSTRAINT `fk__materials__groups__material_id` FOREIGN KEY (`material_id`) REFERENCES `material` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
