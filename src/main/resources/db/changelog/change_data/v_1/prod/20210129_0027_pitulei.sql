-- liquibase formatted sql

-- changeSet Pitulei:password_reset_token endDelimiter:; splitStatements:true

-- PasswordResetToken
CREATE TABLE if not exists `password_reset_token`(
     `id`            int AUTO_INCREMENT PRIMARY KEY,
     `created_at`    datetime    NOT NULL,
     `updated_at`    datetime    DEFAULT NULL,
     `token`         varchar(255) DEFAULT NULL,
     `expiration_date` datetime    DEFAULT NULL,
     `user_id`       int  DEFAULT NULL,
     CONSTRAINT `fk_password_reset_token_user`
         FOREIGN KEY (`user_id`) REFERENCES users (`id`)
);
