package com.softserve.itacademy.enums;

public enum ErrorType {
    NOT_FOUND("NOT_FOUND");

    private String value;

    ErrorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
