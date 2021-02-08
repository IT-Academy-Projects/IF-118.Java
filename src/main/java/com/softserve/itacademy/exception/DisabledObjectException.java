package com.softserve.itacademy.exception;
//add javadocs to each exception to have proper understanding how and whereto use this exception
public class DisabledObjectException extends RuntimeException {

    public DisabledObjectException(String message) {
        super(message);
    }

}
