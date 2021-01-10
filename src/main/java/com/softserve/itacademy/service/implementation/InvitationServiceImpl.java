package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Invitation;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.repository.InvitationRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;
import com.softserve.itacademy.service.InvitationService;
import com.softserve.itacademy.service.MailSender;
import com.softserve.itacademy.service.converters.InvitationConverter;
import org.springframework.stereotype.Service;

@Service
public class InvitationServiceImpl implements InvitationService {
    private final MailSender mailSender;
    private final InvitationConverter invitationConverter;
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;

    public InvitationServiceImpl(MailSender mailSender, InvitationConverter invitationConverter,
                                 InvitationRepository invitationRepository, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.invitationConverter = invitationConverter;
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public InvitationResponse sendInvitation(InvitationRequest invitationRequest) {
        if (userRepository.existsByEmail(invitationRequest.getEmail())) {
            sendInvitationMail(invitationConverter.of(invitationRequest));
        } else {
            sendRegistrationMail(invitationConverter.of(invitationRequest));
        }
        return invitationConverter.of(invitationRepository.save(invitationConverter.of(invitationRequest)));
    }

    @Override
    public InvitationResponse approve(Integer id) {
        return invitationConverter.of(invitationRepository.update(id));
    }

    @Override
    public InvitationResponse setUserId(Integer id) {
        return invitationConverter.of(invitationRepository.setUserId(id));
    }

    private void sendInvitationMail(Invitation invitation) {
        User user = userRepository.getOne(invitation.getUser().getId());
        mailSender.send(user.getEmail(), "SoftClass invitation",
                getInviteMessage(invitation) + " " + invitation.getLink() + user.getId() + "/invite");
    }

    private void sendRegistrationMail(Invitation invitation) {
        mailSender.send(invitation.getEmail(), "SoftClass invitation",
                getInviteMessage(invitation) + " http://localhost:8080/registration");

    }

    private String getInviteMessage(Invitation invitation) {
        String inviteTo = invitation.getGroup() != null ? invitation.getGroup().getName() : invitation.getCourse().getName();
        return String.format("You are invited to %s", inviteTo);
    }

}

