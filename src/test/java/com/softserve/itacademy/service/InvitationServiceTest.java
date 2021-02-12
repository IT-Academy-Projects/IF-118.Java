package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.Invitation;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.InvitationRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;
import com.softserve.itacademy.service.converters.InvitationConverter;
import com.softserve.itacademy.service.implementation.InvitationServiceImpl;
import static java.util.Optional.of;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
public class InvitationServiceTest {

    @InjectMocks
    private InvitationServiceImpl invitationService;
    @Mock
    private MailDesignService mailDesignService;
    @Mock
    private InvitationConverter invitationConverter;
    @Mock
    private InvitationRepository invitationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private CourseRepository courseRepository;

    @Test
    void sendInvitationSuccessfulTest() {
        InvitationRequest request = buildRequest();
        setUp();
        doNothing().when(mailDesignService).designAndQueue(anyString(), anyString(), anyString());

        invitationService.sendInvitation(request);
        verify(invitationRepository, times(1)).save(any());
    }

    @Test
    void approveCourseByLinkSuccessfulTest() {
        approveCourseSetUp();
        invitationService.approveByLink("link", "code");
        verify(groupRepository, times(1)).save(any());

    }

    @Test
    void approveGroupByLinkSuccessfulTest() {
        approveGroupSetUp();

        invitationService.approveByLink("link", "code");
        verify(invitationRepository, times(1)).groupApprove(anyInt(), anyInt());
    }

    @Test
    void approveGroupByIdSuccessfulTest() {
        when(invitationRepository.findById(anyInt())).thenReturn(of(buildInvitation()));
        when(invitationConverter.of(any(Invitation.class))).thenReturn(buildResponse());
        approveGroupSetUp();
        invitationService.approveById(1);
        verify(invitationRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void approveCourseByIdSuccessfulTest() {
        when(invitationRepository.findById(anyInt())).thenReturn(of(buildInvitation()));
        when(invitationConverter.of(any(Invitation.class))).thenReturn(buildResponse());
        approveCourseSetUp();
        invitationService.approveById(1);
        verify(invitationRepository, times(1)).deleteById(anyInt());
    }

    private void approveGroupSetUp() {
        Invitation invitation = buildInvitation();
        invitation.setCourse(null);
        invitation.setGroup(buildGroup());
        when(invitationRepository.findByCode(anyString())).thenReturn(of(invitation));
        when(invitationRepository.approve(anyInt(), anyString())).thenReturn(1);

    }

    private void approveCourseSetUp() {
        when(invitationRepository.findByCode(anyString())).thenReturn(of(buildInvitation()));
        when(invitationRepository.approve(anyInt(), anyString())).thenReturn(1);
    }

    private void setUp() {
        Invitation invitation = buildInvitation();
        when(userRepository.findById(anyInt())).thenReturn(of(buildUser()));
        when(groupRepository.findById(anyInt())).thenReturn(of(buildGroup()));
        when(courseRepository.findById(anyInt())).thenReturn(of(buildCourse()));
        when(invitationConverter.of(any(InvitationRequest.class))).thenReturn(invitation);
        when(invitationRepository.save(any())).thenReturn(invitation);
    }

    private InvitationRequest buildRequest() {
        return InvitationRequest.builder()
                .userId(1)
                .courseId(1)
                .ownerId(1)
                .email("some@mail.com")
                .build();
    }

    private Invitation buildInvitation() {
        Invitation inv = Invitation.builder()
                .user(buildUser())
                .course(buildCourse())
                .link("link")
                .ownerId(2)
                .email("some@email.com")
                .code("code")
                .expirationDate(LocalDateTime.now().plusDays(1))
                .approved(false)
                .build();
        inv.setId(1);
        return inv;
    }

    private User buildUser() {
        User user = User.builder()
                .disabled(false)
                .name("name")
                .password("password")
                .email("email")
                .invitationCode("invitationCode")
                .build();
        user.setId(1);
        return user;
    }

    private Course buildCourse() {
        Course course = Course.builder()
                .name("course")
                .ownerId(2)
                .description("description")
                .disabled(false)
                .build();
        course.setId(1);
        return course;
    }
    private Group buildGroup() {
        Group group = Group.builder()
                .disabled(false)
                .name("name")
                .ownerId(2)
                .courses(Collections.emptySet())
                .users(Collections.emptySet())
                .assignments(Collections.emptySet())
                .build();
        group.setId(1);
        return group;
    }
    private InvitationResponse buildResponse() {
        return InvitationResponse.builder()
                .ownerId(1)
                .code("code")
                .approved(false)
                .link("link")
                .courseOrGroup("course")
                .id(1)
                .build();
    }
}
