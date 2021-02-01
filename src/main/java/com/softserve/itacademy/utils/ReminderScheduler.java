package com.softserve.itacademy.utils;

import com.softserve.itacademy.service.implementation.MailReminder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReminderScheduler {
    private final MailReminder mailReminder;

    public ReminderScheduler(MailReminder mailReminder) {
        this.mailReminder = mailReminder;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendRemindByMail() {
        mailReminder.remind();
    }
}
