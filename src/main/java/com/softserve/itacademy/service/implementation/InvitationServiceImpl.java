package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Invitation;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.InvitationRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;
import com.softserve.itacademy.service.InvitationService;
import com.softserve.itacademy.service.MailSender;
import com.softserve.itacademy.service.converters.InvitationConverter;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
            invitation.setLink(getLink(invitation));
        } else {
            throw new NotFoundException("user with such email was not found");
        }
        return invitationConverter.of(invitationRepository.save(invitation));
    }

    @Override
    public void approve(String email, String code) {
        Invitation invitation = invitationRepository.findByCode(code);
        if (invitation.getGroup() != null) {
            invitationRepository.update(invitation.getUser().getId());
            invitationRepository.groupApprove(invitation.getUser().getId(), invitation.getGroup().getId());
        }
        else {
            invitationRepository.update(invitation.getUser().getId());
            invitationRepository.courseApprove(invitation.getUser().getId(), invitation.getCourse().getId());
        }
    }

    private void sendInvitationMail(Invitation invitation) {
        User user = userRepository.getOne(invitation.getUser().getId());
        mailSender.send(user.getEmail(), "SoftClass invitation",
                getInviteMessage(invitation));
    }

    private String getInviteMessage(Invitation invitation) {
        String inviteTo = invitation.getGroup() != null ? invitation.getGroup().getName() : invitation.getCourse().getName();
        return String.format("You are invited to %s %s", inviteTo, getLink(invitation));
    }

    private String getLink(Invitation invitation) {
        String courseOrGroup = invitation.getGroup() != null ? "group?id=" : "course?id=";
        Integer id = courseOrGroup.equals("group?id=") ? invitation.getGroup().getId() : invitation.getCourse().getId();
        return invitation.getLink() + invitation.getEmail() + "/" + invitation.getCode();
    }
}

