package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.service.MailDesignService;
import com.softserve.itacademy.service.MailService;
import com.softserve.itacademy.service.mailing.MailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class MailDesignServiceImpl implements MailDesignService {

    private final MailService mailSender;

    public MailDesignServiceImpl(MailService mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void designAndQueue(String email, String topic, Map<String, Object> mailContext, MailType mailType) {
        MailMessage mail = MailMessage.builder()
                .email(email)
                .subject(topic)
                .mailContext(mailContext)
                .mailTemplate(mailType.getTemplate())
                .expirationDate(LocalDateTime.now().plusDays(3))
                .build();

        mailSender.addMessageToMailQueue(mail);
    }

    public enum MailType{
        ACTIVATION("activation-temp"),
        INVITATION("invitation-temp"),
        RESET_PASSWORD("reset-temp"),
        LECTION_OPEN("open-lection-temp"),
        LECTION_EXPIRATION("expiation-reminder-temp");

        private String template;

        MailType(String template) {
            this.template = template;
        }

        public String getTemplate() {
            return template;
        }
    }
}
