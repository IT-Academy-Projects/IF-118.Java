-- liquibase formatted sql
-- changeSet Babiy:event_system endDelimiter:; splitStatements:true

CREATE TABLE if not exists `events`(
    `id`        INT AUTO_INCREMENT PRIMARY KEY,
    `date` datetime                NOT NULL,
    `type` varchar(30)             NOT NULL,
    `recipient_id`     int         NOT NULL,
    `creator_id`       int         NOT NULL,
    `subject_id`       int         NOT NULL,
    CONSTRAINT `fk_events_users_creator_id` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_events_users_recipient_id` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`id`)
);