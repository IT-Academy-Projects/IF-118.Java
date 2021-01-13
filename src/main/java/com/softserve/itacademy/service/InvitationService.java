package com.softserve.itacademy.service;

import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;

public interface InvitationService {

    InvitationResponse sendInvitation(InvitationRequest invitationRequest);
    void approve (String email, String code);
}
