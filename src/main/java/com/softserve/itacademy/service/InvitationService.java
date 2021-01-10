package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Invitation;
import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;

public interface InvitationService {

    InvitationResponse sendInvitation(InvitationRequest invitationRequest);
    InvitationResponse approve (Integer id);
    InvitationResponse setUserId(Integer id);
}
