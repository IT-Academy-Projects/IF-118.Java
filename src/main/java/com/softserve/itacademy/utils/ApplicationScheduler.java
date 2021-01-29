package com.softserve.itacademy.utils;


import com.softserve.itacademy.service.InvitationService;
import com.softserve.itacademy.service.PasswordResetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationScheduler {

    private final InvitationService invitationService;
    private final PasswordResetService passwordResetService;

    public ApplicationScheduler(InvitationService invitationService, PasswordResetService passwordResetService) {
        this.invitationService = invitationService;
        this.passwordResetService = passwordResetService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredInvites() {
        log.info("Invitation cleaner start");
        int countOfDeleted = invitationService.deleteByExpirationDate();
        log.info("Invitation cleaning finished: " + countOfDeleted + " invitations have been cleaned");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredPassResetTokens() {
        log.info("Reset Tokens cleaner start:");
        int countOfDeleted = passwordResetService.deleteExpiredTokens();
        log.info("Reset Tokens cleaning finished: " + countOfDeleted + " tokens have been cleaned");
    }
}
