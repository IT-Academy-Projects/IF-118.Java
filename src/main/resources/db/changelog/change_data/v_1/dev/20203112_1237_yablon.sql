# -- liquibase formatted sql
#
# -- changeSet YabVol:change_data-1.0.0 endDelimiter:; splitStatements:true
#
# -- All passwords are: password, password1, password2, passwordst1, passwordst2, passwordst3, passwordst4
# -- INSERT TEACHERS
# insert into users(name, password, email, created_at, updated_at, disabled)
# values ('Teacher 1', '$2a$08$HNNmFKIjddTd.viOWqOP.OaIvbtQzkNdYe/D7GfVEjcg/lw0HezYS', 'teacher1@gmail.com', '2020-12-23 11:40:09', '2020-12-23 11:40:09', true);
#
# insert into users(name, password, email, created_at, updated_at)
# values ('Teacher 2', '$2a$08$v3MRGTNOHykHKpwImRvOHO32Vq1qKI3njWPT1GmPZpnWLgz6aiIK6', 'teacher2@gmail.com', '2020-12-23 12:40:09', '2020-12-23 12:40:09');
#
# insert into users(name, password, email, created_at, updated_at)
# values ('Teacher 3', '$2a$08$zJXqjONe/dTzN1nN3b0XveWTwzyU9MOOGoTo9.bkVq/kfF8W.QpMK', 'teacher3@gmail.com', '2020-12-23 13:40:09', '2020-12-23 13:40:09');
#
# -- INSERT STUDENTS
# insert into users(name, password, email, created_at, updated_at)
# values ('Student 1', '$2a$08$2iV6AjAa0N8mECoUjAOxg.oCu/TZs3ZM7b9XGds7GvUB0JtdmBvbq', 'student1@gmail.com', '2020-12-23 11:40:09', '2020-12-23 11:40:09');
#
# insert into users(name, password, email, created_at, updated_at)
# values ('Student 2', '$2a$08$2CfIZNC.I19/W.O.8w5BpOVU1ozn8bWzDM99ocaAlIGEErE2nzSSK', 'student2@gmail.com', '2020-12-23 12:40:09', '2020-12-23 12:40:09');
#
# insert into users(name, password, email, created_at, updated_at)
# values ('Student 3', '$2a$08$wZA1pXskRyO7FYSV2qcUjOPybboVw9grE40LQL2I6xXIqCAA4ycoG', 'student3@gmail.com', '2020-12-23 13:40:09', '2020-12-23 13:40:09');
#
# insert into users(name, password, email, created_at, updated_at)
# values ('Student 4', '$2a$08$Ypcsdxl1BqnexQPnNKePn.ZdgDpvH8jHgiURDpAL0wbX5uyWRTP9.', 'student4@gmail.com', '2020-12-23 13:40:09', '2020-12-23 13:40:09');
#
# -- INSERT COURSES
# insert into courses(name, owner_id, created_at, updated_at)
# values ('Java Course', '1', '2020-12-23 14:40:09', '2020-12-23 14:40:09');
#
# insert into courses(name, owner_id, created_at, updated_at)
# values ('C# Course', '2', '2018-10-15 14:40:09', '2020-12-23 14:40:09');
#
# insert into courses(name, owner_id, created_at, updated_at)
# values ('Ruby Course', '3', '2020-03-17 14:40:09', '2020-12-23 14:40:09');
#
# -- INSERT STUDENT GROUPS
# insert into student_groups(name, owner_id, created_at, updated_at)
# values ('IF-118', '1', '2020-12-23 14:40:09', '2020-12-23 14:40:09');
#
# insert into student_groups(name, owner_id, created_at, updated_at)
# values ('LV-120', '2', '2020-12-23 14:40:09', '2020-12-23 14:40:09');
#
# -- INSERT INTO GROUPS_USERS
# insert into groups_users(group_id, user_id)
# values ('1', '4');
#
# insert into groups_users(group_id, user_id)
# values ('1', '5');
#
# insert into groups_users(group_id, user_id)
# values ('2', '6');
#
# insert into groups_users(group_id, user_id)
# values ('2', '7');
    