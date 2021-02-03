-- liquibase formatted sql

-- changeSet Tsoi:delete_assignment-material_mapping endDelimiter:; splitStatements:true

ALTER TABLE assignment
DROP CONSTRAINT `fk_material_assignment`;

ALTER TABLE assignment
    DROP COLUMN material_id;

-- changeSet Tsoi:add_assignment-groups_mapping endDelimiter:; splitStatements:true

ALTER TABLE assignment
ADD COLUMN group_id int NOT NULL;

-- changeSet Tsoi:create_table_groups_assignments endDelimiter:; splitStatements:true
CREATE TABLE if not exists `groups_assignments`
(
    `group_id`  int NOT NULL,
    `assignment_id` int NOT NULL,
    PRIMARY KEY (`group_id`, `assignment_id`),
    KEY `fk__courses__groups__assignment_id` (`assignment_id`),
    CONSTRAINT `fk__courses__groups__assignment_id` FOREIGN KEY (`assignment_id`) REFERENCES `assignment` (`id`),
    CONSTRAINT `fk__groups__assignments__group_id` FOREIGN KEY (`group_id`) REFERENCES `student_groups` (`id`)
);

-- changeSet Tsoi:add_column_owner_id_to_invitations endDelimiter:; splitStatements:true

ALTER TABLE invitation
    ADD COLUMN owner_id int NOT NULL;