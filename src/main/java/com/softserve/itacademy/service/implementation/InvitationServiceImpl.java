package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Invitation;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.InvitationServiceException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.InvitationRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;
import com.softserve.itacademy.service.InvitationService;
import com.softserve.itacademy.service.MailSender;
import com.softserve.itacademy.service.converters.InvitationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.info("sending invitation on email");
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
    public InvitationResponse approveByLink(String email, String code) {
        Invitation invitation = invitationRepository.findByCode(code)
                .orElseThrow(() -> new InvitationServiceException("No such invitation"));
        log.info("approving invitation");
        return approveCourseOrGroup(invitation);
    }

    @Override
    public void delete(Integer id) {
        log.info("disapproving invitation");
        invitationRepository.delete(getById(id));
    }

    @Override
    public void approveById(Integer id) {
        log.info("approving invitation");
        InvitationResponse invitationResponse = approveCourseOrGroup(getById(id));
        invitationRepository.approve(id, invitationResponse.getCode());
    }

    @Override
    public List<InvitationResponse> findAllByEmail(String email) {
        log.info("looking for all invitations of the user");
        return invitationRepository.findAllByEmail(email).stream()
                .filter(invitation -> !invitation.getApproved())
                .map(invitationConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public int deleteByExpirationDate() {
        return invitationRepository.deleteByExpirationDate();
    }

    @Override
    public List<InvitationResponse> findAll() {
        return invitationRepository.findAll().stream()
                .map(invitationConverter::of)
                .collect(Collectors.toList());
    }

    private InvitationResponse approveCourseOrGroup(Invitation invitation) {
        if (!invitation.getApproved() && invitation.getExpirationDate().isAfter(LocalDateTime.now())) {
            if (invitation.getGroup() != null && canBeApproved(invitation)) {
                approve(invitation);
                invitationRepository.groupApprove(invitation.getUser().getId(), invitation.getGroup().getId());
                return invitationConverter.of(invitationRepository.findByCode(invitation.getCode()).get());
            } else {
                approve(invitation);
                invitationRepository.courseApprove(invitation.getUser().getId(), invitation.getCourse().getId());
                return invitationConverter.of(invitationRepository.findByCode(invitation.getCode()).get());
            }
        }
        return InvitationResponse.builder()
                .approved(false)
                .build();
    }

    private boolean canBeApproved(Invitation invitation) {
        return !invitation.getApproved()
                && invitation.getExpirationDate().isAfter(LocalDateTime.now());
    }

    private void approve(Invitation invitation) {
        invitationRepository.approve(invitation.getUser().getId(), invitation.getCode());
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
//        String courseOrGroup = invitation.getGroup() != null ? "group?id=" : "course?id=";
//        Integer id = courseOrGroup.equals("group?id=") ? invitation.getGroup().getId() : invitation.getCourse().getId();
        return invitation.getLink() + invitation.getEmail() + "/" + invitation.getCode();
    }

    private Invitation getById(Integer id) {
        return invitationRepository.findById(id).orElseThrow(() -> new NotFoundException("Invitation with such id was not found"));
    }
}

