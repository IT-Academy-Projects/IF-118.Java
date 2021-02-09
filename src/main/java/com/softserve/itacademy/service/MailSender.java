package com.softserve.itacademy.service;

import com.softserve.itacademy.service.mailing.MailMessage;

public interface MailSender {

    void addMessageToMailQueue(MailMessage message);
}
