package com.softserve.itacademy.service;

import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;

public interface InvitationService {

    InvitationResponse sendInvitation(InvitationRequest invitationRequest);

    InvitationResponse approve(String email, String code);

}
