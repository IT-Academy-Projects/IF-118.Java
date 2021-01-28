-- liquibase formatted sql
-- changeSet Babiy:resources endDelimiter:; splitStatements:true

--Image
CREATE TABLE if not exists `images`(
    `id`         int                    NOT NULL AUTO_INCREMENT,
    `created_at` datetime               NOT NULL,
    `updated_at` datetime               DEFAULT NULL,
    `file` mediumblob                   DEFAULT NULL,
    PRIMARY KEY (`id`)
);

--User
ALTER TABLE users
DROP COLUMN avatar;

ALTER TABLE users
ADD COLUMN `image_id` int DEFAULT NULL,
ADD CONSTRAINT `fk_users_images_image_id` FOREIGN KEY (`image_id`) REFERENCES images (`id`);

--Group
ALTER TABLE `student_groups`
DROP COLUMN avatar;

ALTER TABLE `student_groups`
ADD COLUMN `image_id` int DEFAULT NULL,
ADD CONSTRAINT `fk_student_groups_images_image_id` FOREIGN KEY (`image_id`) REFERENCES images (`id`);

--Course
ALTER TABLE `courses`
DROP COLUMN avatar;

ALTER TABLE `courses`
ADD COLUMN `image_id` int DEFAULT NULL,
ADD CONSTRAINT `fk_courses_images_image_id` FOREIGN KEY (`image_id`) REFERENCES images (`id`);

--Assignment
ALTER TABLE `assignment`
ADD COLUMN `file_reference` varchar(255) DEFAULT NULL