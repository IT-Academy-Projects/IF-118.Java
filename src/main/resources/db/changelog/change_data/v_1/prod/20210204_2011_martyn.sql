-- liquibase formatted sql
-- changeSet Martyn:drop columns endDelimiter:; splitStatements:true

  -- ASSIGNMENT_ANSWERS
   ALTER TABLE assignment_answers
    DROP COLUMN is_reviewed_by_teacher, DROP COLUMN is_student_saw_grade;