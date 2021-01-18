package com.softserve.itacademy.utils;


import com.softserve.itacademy.service.InvitationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class InvitationScheduler {

    private final InvitationService invitationService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteByExpirationDate() {
        log.info("Scheduler start:");
        int countOfDeleted = invitationService.deleteByExpirationDate();
        log.info("Scheduler finished: " + countOfDeleted + " invitations have been deleted by expiration date");
    }
}
