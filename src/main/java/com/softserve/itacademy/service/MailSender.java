package com.softserve.itacademy.service;

public interface MailSender {
    void send(String emailTo, String subject, String message);
}
