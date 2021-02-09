package com.softserve.itacademy.service;

import com.softserve.itacademy.service.mailing.MailMessage;

public interface MailService {

    void addMessageToMailQueue(MailMessage message);
}
