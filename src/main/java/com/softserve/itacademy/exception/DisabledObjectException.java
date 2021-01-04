package com.softserve.itacademy.exception;

public class DisabledObjectException extends RuntimeException {
//    TODO bk hardcoded text here doesn't have any sense here. Let's discuss
    public DisabledObjectException() {
        super("Object is disabled");
    }

}
