package com.softserve.itacademy.service;

import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;

import java.util.List;

public interface InvitationService {

    InvitationResponse sendInvitation(InvitationRequest invitationRequest);

    InvitationResponse approveByLink(String email, String code);

    List<InvitationResponse> findAllByEmail(String email);

    void delete(Integer id);

    void approveById(Integer id);
}
