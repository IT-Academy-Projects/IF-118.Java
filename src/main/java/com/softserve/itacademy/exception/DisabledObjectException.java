package com.softserve.itacademy.exception;

public class DisabledObjectException extends RuntimeException {

    public DisabledObjectException() {

    }

    public DisabledObjectException(String message) {
        super(message);
    }

}
