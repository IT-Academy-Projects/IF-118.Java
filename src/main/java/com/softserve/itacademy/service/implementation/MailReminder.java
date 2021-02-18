package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.MailDesignService;
import com.softserve.itacademy.service.Reminder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MailReminder implements Reminder {
    private final MailDesignService mailDesignService;

    public MailReminder(MailDesignService mailDesignService) {
        this.mailDesignService = mailDesignService;
    }

    @Override
    public void remind(List<User> recipients) {
        if (!recipients.isEmpty()) {
            recipients.forEach(user -> mailDesignService.designAndQueue(user.getEmail(), "SoftClass Lection work time expiring",
                    Map.of("name", user.getName(), "message", "Check Your courses on SoftClass. Looks like Your work time on some lections is expiring."),
                    MailDesignServiceImpl.MailType.LECTION_EXPIRATION));
        }
    }
}
