package com.softserve.itacademy.security.perms.roles;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('ROLE_USER')")
public @interface UserRolePermission {
}
