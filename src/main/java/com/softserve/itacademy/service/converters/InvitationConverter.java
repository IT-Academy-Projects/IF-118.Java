package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Invitation;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Component
public class InvitationConverter {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;

    public InvitationResponse of(Invitation invitation) {
        return modelMapper.map(invitation, InvitationResponse.class);
    }

    public Invitation of(InvitationRequest request) {
        return Invitation.builder()
                .createdAt(LocalDateTime.now())
                .expirationDate(LocalDateTime.now().plusDays(7))
                .email(request.getEmail())
                .approved(false)
                .link("http://localhost:8080/api/v1/invitation/approve/")
                .code(UUID.randomUUID().toString())
                .user(userRepository.findByEmail(request.getEmail()).orElse(null))
                .group(groupRepository.findById(request.getGroupId()).orElse(null))
                .course(courseRepository.findById(request.getCourseId()).orElse(null))
                .build();
    }
}
