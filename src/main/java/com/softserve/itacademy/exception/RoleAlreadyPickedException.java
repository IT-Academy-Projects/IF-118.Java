package com.softserve.itacademy.exception;

import org.springframework.security.core.AuthenticationException;

public class RoleAlreadyPickedException extends AuthenticationException {
    public RoleAlreadyPickedException(String msg) {
        super(msg);
    }
}
