package com.softserve.itacademy.tools.security;

import com.softserve.itacademy.tools.security.WithMockOwnUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockOwnUser(name = "Admin", role = "ADMIN")
public @interface WithMockOwnAdmin {
}
