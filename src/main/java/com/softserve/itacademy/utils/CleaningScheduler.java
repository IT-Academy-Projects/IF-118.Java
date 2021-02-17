package com.softserve.itacademy.utils;


import com.softserve.itacademy.response.MaterialExpirationResponse;
import com.softserve.itacademy.service.InvitationService;
import com.softserve.itacademy.service.MaterialExpirationService;
import com.softserve.itacademy.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class CleaningScheduler {

    private final InvitationService invitationService;
    private final MaterialExpirationService materialExpirationService;
    private final MaterialService materialService;

    public CleaningScheduler(InvitationService invitationService, MaterialExpirationService materialExpirationService, MaterialService materialService) {
        this.invitationService = invitationService;
        this.materialExpirationService = materialExpirationService;
        this.materialService = materialService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredInvites() {
        log.info("Invitation cleaner start");
        int countOfDeleted = invitationService.deleteByExpirationDate();
        log.info("Invitation cleaning finished: " + countOfDeleted + " invitations have been cleaned");
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void closeExpiredMaterials() {
        List<MaterialExpirationResponse> expirations = materialExpirationService.findAllExpiringBy(LocalDateTime.now());
        expirations.forEach(expiration->{
            materialService.closeByExpirationDate(expiration.getMaterialId(), expiration.getGroupId());
        });

        materialExpirationService.deleteByExpirationDate();
    }
}