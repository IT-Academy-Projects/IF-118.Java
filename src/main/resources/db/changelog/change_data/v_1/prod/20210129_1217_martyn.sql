-- liquibase formatted sql
-- changeSet Martyn:field_for_check_that_user_viewed_notific_about_new_answer_or_grade endDelimiter:; splitStatements:true

  -- ASSIGNMENT_ANSWERS
   ALTER TABLE assignment_answers
    ADD COLUMN is_reviewed_by_teacher BOOLEAN DEFAULT false AFTER grade,
    ADD COLUMN is_student_saw_grade BOOLEAN DEFAULT false AFTER is_reviewed_by_teacher;