package com.softserve.itacademy.tools.security;

import com.softserve.itacademy.tools.security.WithMockOwnUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockOwnUserSecurityContextFactory.class)
@interface WithMockOwnUser {

    String name() default "Test";

    String role();
}
