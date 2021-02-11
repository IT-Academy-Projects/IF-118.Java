package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.service.MailService;
import com.softserve.itacademy.service.mailing.MailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final QueueMessagingTemplate queueMessagingTemplate;

    public MailServiceImpl(JavaMailSender mailSender, QueueMessagingTemplate queueMessagingTemplate) {
        this.mailSender = mailSender;
        this.queueMessagingTemplate = queueMessagingTemplate;
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
    public void loadMessageFromSQS(MailMessage message) {
        send(message);
    }

    public void send(MailMessage message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(message.getEmail());
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getMessage());

        log.info("Sending message to {} with {} subject", message.getEmail(), message.getSubject());

        mailSender.send(mailMessage);
    }
}
