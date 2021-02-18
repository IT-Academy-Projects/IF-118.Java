package com.softserve.itacademy.service;

import com.softserve.itacademy.service.mailing.MailMessage;

import javax.mail.MessagingException;

public interface MailService {

    void addMessageToMailQueue(MailMessage message);

    void send(MailMessage message) throws MessagingException;
}
