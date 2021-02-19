package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.service.MailService;
import com.softserve.itacademy.service.mailing.MailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

@Service
@Profile("!test")
@Slf4j
public class MailServiceImpl implements MailService {

    @Value("${application.address}")
    private String address;

    private final JavaMailSender mailSender;
    private final QueueMessagingTemplate queueMessagingTemplate;
    private final TemplateEngine templateEngine;

    public MailServiceImpl(JavaMailSender mailSender, QueueMessagingTemplate queueMessagingTemplate, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.username}")
    private String username;

    @Value("${cloud.aws.sqs.end-point.uri}")
    private String endpoint;

    @Override
    public void addMessageToMailQueue(MailMessage message) {
        queueMessagingTemplate.convertAndSend(endpoint, message);
    }

    @SqsListener(value = "${cloud.aws.sqs.end-point.uri}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 300))
    public void loadMessageFromSQS(MailMessage message) throws MessagingException {
        log.info("Loading message to {} with {} subject from queue", message.getEmail(), message.getSubject());
        validateAndSend(message);
    }

    public boolean validateAndSend(MailMessage message) throws MessagingException {
        if (message.getExpirationDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        send(message);
        return true;
    }

    @Override
    public void send(MailMessage message) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);

        Context context = new Context();
        context.setVariables(message.getMailContext());
        context.setVariable("logo", "logo");
        context.setVariable("address", address);

        String process = templateEngine.process(message.getMailTemplate(), context);

        helper.setSubject(message.getSubject());
        helper.setText(process, true);
        helper.addInline("logo", new ClassPathResource("static/img/SoftClassLogo.png"), "image/png");
        helper.setTo(message.getEmail());
        helper.setFrom(username);

        log.info("Sending message to {} with {} subject", message.getEmail(), message.getSubject());

        mailSender.send(mailMessage);
    }
}
