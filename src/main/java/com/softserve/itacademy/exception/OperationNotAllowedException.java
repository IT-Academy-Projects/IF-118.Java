package com.softserve.itacademy.exception;

public class OperationNotAllowedException extends RuntimeException {

    public OperationNotAllowedException() {

    }

    public OperationNotAllowedException(String message) {
        super(message);
    }

}
