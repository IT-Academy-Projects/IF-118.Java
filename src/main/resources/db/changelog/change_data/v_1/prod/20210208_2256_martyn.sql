-- liquibase formatted sql
-- changeSet Martyn:create_table_events_recipients endDelimiter:; splitStatements:true

  -- EVENTS
  ALTER TABLE events
    DROP FOREIGN KEY  `fk_events_users_recipient_id`;

    ALTER TABLE events
     DROP COLUMN recipient_id;

   -- EVENTS_RECIPIENTS
    CREATE TABLE IF NOT EXISTS `events_recipients` (
    `event_id` INT NOT NULL,
    `recipient_id` INT NOT NULL,
    PRIMARY KEY (`event_id`, `recipient_id`),
    CONSTRAINT `fk_events_users_recipient_id` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_events_users_event_id` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`)
)
