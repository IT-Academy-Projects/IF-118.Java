-- liquibase formatted sql

-- changeSet Pitulei:tables_for_chat endDelimiter:; splitStatements:true

-- GroupChatRoom
CREATE TABLE if not exists `group_chat`(
    `id`        INT AUTO_INCREMENT PRIMARY KEY,
    `created_at` datetime NOT NULL,
    `updated_at` datetime DEFAULT NULL,
    `group_id`  int       DEFAULT NULL,
    CONSTRAINT `fk_group_chat_message`
        FOREIGN KEY (`group_id`) REFERENCES student_groups (`id`)
);

-- ChatMessage
CREATE TABLE if not exists `chat_message`(
    `id`            int AUTO_INCREMENT PRIMARY KEY,
    `created_at`    datetime    NOT NULL,
    `updated_at`    datetime    DEFAULT NULL,
    `content`       varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    `status`        int         DEFAULT NULL,
    `group_chat_id` int         DEFAULT NULL,
    `user_id`       int         DEFAULT NULL,
    CONSTRAINT `fk_message_group_chat`
        FOREIGN KEY (`group_chat_id`) REFERENCES group_chat (`id`),
    CONSTRAINT `fk_user_message`
        FOREIGN KEY (`user_id`) REFERENCES users (`id`)
);