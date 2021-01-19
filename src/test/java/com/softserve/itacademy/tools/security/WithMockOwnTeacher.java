package com.softserve.itacademy.tools.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockOwnUser(name = "Teacher", role = "TEACHER")
public @interface WithMockOwnTeacher {
}
