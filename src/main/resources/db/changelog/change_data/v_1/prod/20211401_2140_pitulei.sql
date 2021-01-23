-- liquibase formatted sql

-- changeSet Pitulei:tables_for_chat endDelimiter:; splitStatements:true

-- GroupChatRoom
CREATE TABLE if not exists `chat_room`(
    `id`        INT AUTO_INCREMENT PRIMARY KEY,
    `created_at` datetime NOT NULL,
    `updated_at` datetime DEFAULT NULL,
    `group_id`  int       DEFAULT NULL
);

-- ChatMessage
CREATE TABLE if not exists `chat_message`(
    `id`            int AUTO_INCREMENT PRIMARY KEY,
    `created_at`    datetime    NOT NULL,
    `updated_at`    datetime    DEFAULT NULL,
    `content`       varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    `status`        int         DEFAULT NULL,
    `chat_room_id` int         DEFAULT NULL,
    `user_id`       int         DEFAULT NULL,
    CONSTRAINT `fk_message_group_chat`
        FOREIGN KEY (`chat_room_id`) REFERENCES chat_room (`id`),
    CONSTRAINT `fk_message_user`
        FOREIGN KEY (`user_id`) REFERENCES users (`id`)
);

-- Groups
ALTER TABLE student_groups
    ADD COLUMN chat_room_id int DEFAULT NULL AFTER owner_id,
    ADD CONSTRAINT `fk_group_chat_room`
    FOREIGN KEY (`chat_room_id`) REFERENCES chat_room (`id`)