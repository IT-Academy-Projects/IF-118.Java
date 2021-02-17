package com.softserve.itacademy.utils;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.response.MaterialExpirationResponse;
import com.softserve.itacademy.service.MaterialExpirationService;
import com.softserve.itacademy.service.implementation.MailReminder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReminderScheduler {
    private final MailReminder mailReminder;
    private final MaterialExpirationService materialExpirationService;
    private final UserRepository userRepository;

    public ReminderScheduler(MailReminder mailReminder, MaterialExpirationService materialExpirationService, UserRepository userRepository) {
        this.mailReminder = mailReminder;
        this.materialExpirationService = materialExpirationService;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendRemindByMail() {
        List<MaterialExpirationResponse> expirations = materialExpirationService.findAllExpiringBy(LocalDateTime.now().plusDays(1));
        if (!expirations.isEmpty()) {
            List<Integer> groupIds = expirations.stream()
                    .map(MaterialExpirationResponse::getGroupId)
                    .collect(Collectors.toList());
            List<User> usersByGroupIds = userRepository.findAllByGroupIds(groupIds);
            mailReminder.remind(usersByGroupIds);
        }
    }
}
