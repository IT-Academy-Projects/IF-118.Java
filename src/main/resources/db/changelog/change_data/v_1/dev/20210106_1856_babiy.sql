# -- liquibase formatted sql
# -- changeSet Babiy:add_users_roles endDelimiter:; splitStatements:true
#
#
# INSERT INTO users_roles (user_id, role_id)
# VALUES (5, 1),
#        (4, 2);
#
# INSERT INTO groups_courses (group_id, course_id)
# VALUES (1, 1),
#        (1, 2);
#
# INSERT INTO users_courses (user_id, course_id)
# VALUES (4, 1),
#        (4, 2);
