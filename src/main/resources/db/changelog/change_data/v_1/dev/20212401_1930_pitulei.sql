-- liquibase formatted sql
-- changeSet Pitulei:group_chats_for_default_groups endDelimiter:; splitStatements:true

-- CHAT ROOMS
INSERT INTO chat_room (id, created_at, updated_at, type)
VALUES (1, NOW(), NOW(), 'GROUP'),
       (2, NOW(), NOW(), 'GROUP');

-- GROUPS
UPDATE student_groups g
SET g.chat_room_id = 1
WHERE g.id = 1;

UPDATE student_groups g
SET g.chat_room_id = 2
WHERE g.id = 2;

