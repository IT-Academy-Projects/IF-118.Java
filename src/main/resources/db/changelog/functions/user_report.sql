-- liquibase formatted sql
-- changeset tsoi:get_user's_report_by_group runOnChange:true splitStatements:false stripComments:false

DROP PROCEDURE IF EXISTS softclass.get_user_report;

CREATE PROCEDURE softclass.get_user_report(IN group_id int,
IN owner_id int)
BEGIN
    SELECT a.id, a.description, aa.id, aa.grade
    FROM assignment a
             LEFT JOIN assignment_answers aa on a.id = aa.assignment_id
             JOIN groups_assignments ga on a.id = ga.assignment_id
    where ga.group_id = group_id and (aa.owner_id = owner_id or owner_id IS NULL);
END;
DELIMITER ;
