package com.softserve.itacademy.exception;

public class DisabledObjectException extends RuntimeException {

    public DisabledObjectException() {
        super("Object is disabled");
    }

}
