-- liquibase formatted sql
-- changeSet Martyn:change_data-1.0.0 endDelimiter:; splitStatements:true

CREATE TABLE IF NOT EXISTS `invitation` (
    `id` INT NOT NULL,
    `created_at` DATETIME NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `expiration_date` DATETIME NOT NULL,
    `approved` BOOLEAN DEFAULT FALSE ,
    `link` VARCHAR(255) NOT NULL,
    `user_id` INT DEFAULT NULL,
    `course_id` INT DEFAULT NULL,
    `group_id` INT DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_user_invitation` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_course_invitation` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
    CONSTRAINT `fk_group_invitation` FOREIGN KEY (`group_id`) REFERENCES `student_groups` (`id`)
);
