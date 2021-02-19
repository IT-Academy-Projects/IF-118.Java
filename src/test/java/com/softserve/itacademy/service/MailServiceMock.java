package com.softserve.itacademy.service;

import com.softserve.itacademy.service.mailing.MailMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@Profile("test")
public class MailServiceMock implements MailService {


    @Override
    public void addMessageToMailQueue(MailMessage message) {

    }

    @Override
    public void send(MailMessage message) throws MessagingException {

    }
}
