package com.softserve.itacademy.service;

public interface MailDesignService {
    void designAndQueue(String email, String topic, String message);
}
