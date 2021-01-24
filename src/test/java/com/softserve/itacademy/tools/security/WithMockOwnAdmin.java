package com.softserve.itacademy.tools.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockOwnUser(name = "Admin", role = "ADMIN")
public @interface WithMockOwnAdmin {
}
