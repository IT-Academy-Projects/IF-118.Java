package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Invitation;
import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class InvitationConverter {

    private final ModelMapper modelMapper;

    public InvitationResponse of(Invitation invitation) {
        return modelMapper.map(invitation, InvitationResponse.class);
    }

    public Invitation of (InvitationRequest request) {
        Invitation map = modelMapper.map(request, Invitation.class);
        map.setLink("http://localhost:8080/api/v1/users/");
        return map;
    }
}
