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

    @Scheduled(cron = "0 12 18 * * ?")
    public void deleteByExpirationDate() {
        int sizeBefore = invitationService.findAll().size();
        log.info("Scheduler start:");
        invitationService.deleteByExpirationDate();
        log.info("Scheduler finished: " +
                (sizeBefore - invitationService.findAll().size()) + " invitations have been deleted by expiration date");
    }
}
