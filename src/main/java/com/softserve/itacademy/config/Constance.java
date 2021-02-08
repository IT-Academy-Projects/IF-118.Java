package com.softserve.itacademy.config;

//TODO It's not good to keep all the constants in single class. Better to keep it as closer as possible to the
// related class. And if you decide to extract separate class make it as utility one. And current name is incorrect.
// Fix it
public class Constance {
    public static final String USER_ID_NOT_FOUND = "User with such id was not found";
    public static final String USER_EMAIL_NOT_FOUND = "User with such email was not found";
    public static final String COURSE_ID_NOT_FOUND = "Course with such id was not found";
    public static final String GROUP_ID_NOT_FOUND = "Group with such id was not found";
    public static final String MATERIAL_ID_NOT_FOUND = "Material with such id was not found";
    public static final String COMMENT_ID_NOT_FOUND = "Comment with such id was not found";
    public static final String ASSIGNMENT_ID_NOT_FOUND = "Assignment with such id was not found";
    public static final String ANSWER_ID_NOT_FOUND = "Answer with such id not found";
    public static final String API_V1 = "/api/v1/";
}
