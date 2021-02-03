package com.softserve.itacademy.utils;


import com.softserve.itacademy.service.InvitationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CleaningScheduler {

    private final InvitationService invitationService;

    public CleaningScheduler(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredInvites() {
        log.info("Invitation cleaner start");
        int countOfDeleted = invitationService.deleteByExpirationDate();
        log.info("Invitation cleaning finished: " + countOfDeleted + " invitations have been cleaned");
    }
}
