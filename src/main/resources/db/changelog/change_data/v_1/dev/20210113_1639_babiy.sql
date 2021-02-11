-- liquibase formatted sql
-- changeSet Babiy:add_description_to_courses endDelimiter:; splitStatements:true


UPDATE courses
SET description = 'This is course for learning Java. It was created by Oracle certified programmer.'
WHERE id = 1;

UPDATE courses
SET description = 'This is course for learning C#. Also you will get information about Unity.'
WHERE id = 2;

UPDATE courses
SET description = 'This is course for learning Ruby. It is not so good, better learn Java.'
WHERE id = 3;

