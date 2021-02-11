package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Invitation;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class InvitationConverter {
    @Value("${application.address}")
    private String address;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;

    public InvitationConverter(UserRepository userRepository, GroupRepository groupRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
    }

    public InvitationResponse of(Invitation invitation) {
        String courseOrGroup = invitation.getGroup() == null ? "course" : "group";
        Integer id = courseOrGroup.equals("course") ? invitation.getCourse().getId() : invitation.getGroup().getId();
        return InvitationResponse.builder()
                .id(invitation.getId())
                .courseOrGroup(courseOrGroup)
                .courseOrGroupId(id)
                .link(invitation.getLink())
                .code(invitation.getCode())
                .build();

    }

    public Invitation of(InvitationRequest request) {
        return Invitation.builder()
                .createdAt(LocalDateTime.now())
                .expirationDate(LocalDateTime.now().plusDays(7))
                .email(request.getEmail())
                .approved(false)
                .link(address + "/api/v1/invitation/approve/")
                .code(UUID.randomUUID().toString())
                .user(userRepository.findByEmail(request.getEmail()).orElse(null))
                .group(groupRepository.findById(request.getGroupId()).orElse(null))
                .course(courseRepository.findById(request.getCourseId()).orElse(null))
                .build();
    }
}
