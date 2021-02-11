package com.softserve.itacademy.service;

import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;

import java.util.List;

public interface InvitationService {

    InvitationResponse sendInvitation(InvitationRequest invitationRequest);

    void approveByLink(String email, String code);

    List<InvitationResponse> findAllByEmail(String email);

    void delete(Integer id);

    void approveById(Integer id);

    int deleteByExpirationDate();

    InvitationResponse findByCode(String code);

    InvitationResponse findById(Integer id);
}
