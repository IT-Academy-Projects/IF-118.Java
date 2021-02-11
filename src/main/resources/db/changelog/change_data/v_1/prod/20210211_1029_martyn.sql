-- liquibase formatted sql
-- changeSet Martyn:rename_subject_id_to_entity_id endDelimiter:; splitStatements:true

  -- EVENTS
  ALTER TABLE events
  CHANGE subject_id entity_id INT NOT NULL;
