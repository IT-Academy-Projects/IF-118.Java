package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.service.MailDesignService;
import com.softserve.itacademy.service.MailSender;
import com.softserve.itacademy.service.mailing.MailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailDesignServiceImpl implements MailDesignService {

    private final MailSender mailSender;

    public MailDesignServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void designAndQueue(String email, String topic, String message) {
        MailMessage mail = MailMessage.builder()
                .email(email)
                .subject(topic)
                .message(message)
                .build();

        mailSender.addMessageToMailQueue(mail);
    }
}
