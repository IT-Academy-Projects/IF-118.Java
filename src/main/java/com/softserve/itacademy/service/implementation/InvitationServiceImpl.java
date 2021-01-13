package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Invitation;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.InvitationRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;
import com.softserve.itacademy.service.InvitationService;
import com.softserve.itacademy.service.MailSender;
import com.softserve.itacademy.service.converters.InvitationConverter;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Invitation invitation = invitationConverter.of(invitationRequest);
        if (userRepository.existsByEmail(invitationRequest.getEmail())) {
            sendInvitationMail(invitation);
        } else {
            throw new NotFoundException("user with such email was not found");
        }
        return invitationConverter.of(invitationRepository.save(invitation));
    }

    @Override
    public void approve(Integer id) {
        Optional<Invitation> byId = invitationRepository.findById(id);
        if (byId.get().getExpirationDate().isAfter(byId.get().getCreatedAt())) {
            invitationRepository.update(id);
        }
    }

    private void sendInvitationMail(Invitation invitation) {
        User user = userRepository.getOne(invitation.getUser().getId());
        mailSender.send(user.getEmail(), "SoftClass invitation",
                getInviteMessage(invitation));
    }

    private String getInviteMessage(Invitation invitation) {
        String courseOrGroup = invitation.getGroup() != null ? "group?id=" : "course?id=";
        Integer id = courseOrGroup.equals("group?id=") ? invitation.getGroup().getId() : invitation.getCourse().getId();
        String link = invitation.getLink() + courseOrGroup + id;
        String inviteTo = invitation.getGroup() != null ? invitation.getGroup().getName() : invitation.getCourse().getName();
        return String.format("You are invited to %s %s", inviteTo, link);
    }
}

