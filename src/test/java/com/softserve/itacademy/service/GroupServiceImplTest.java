package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.*;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.*;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.response.*;
import com.softserve.itacademy.service.ChatRoomService;
import com.softserve.itacademy.service.ImageService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.GroupConverter;
import com.softserve.itacademy.service.implementation.GroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;


class GroupServiceImplTest {

    @InjectMocks
    private GroupServiceImpl groupServiceImpl;

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private GroupConverter groupConverter;
    @Mock
    private UserService userService;
    @Mock
    private ChatRoomService chatRoomService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private MaterialRepository materialRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private ImageRepository imageRepository;

    @BeforeEach
    void setupBeforeClass() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateGroupSuccessfully() {
        GroupRequest groupRequest = testGroupRequest();

        when(userService.getById(groupRequest.getOwnerId())).thenReturn(testUser());
        when(groupConverter.of(any(), any())).thenReturn(testGroup());
        when(courseRepository.findByIds(Set.of(1))).thenReturn(Set.of(testCourse()));
        when(materialRepository.findByCourseIds(Set.of(1))).thenReturn(Set.of(testMaterial()));
        when(imageService.compress(any(MultipartFile.class))).thenReturn("content".getBytes());
        when(imageRepository.save(new Image("content".getBytes()))).thenReturn(new Image("content".getBytes()));
        when(chatRoomService.create()).thenReturn(testChatRoom());
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup());
        when(groupConverter.of(any(Group.class))).thenReturn(testGroupResponse());

        GroupResponse groupResponse = groupServiceImpl.create(groupRequest, null);

        assertEquals("TestGroup", groupResponse.getName());
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void testCreateGroupThrowsDisabledObjectException() {
        GroupRequest groupRequest = testGroupRequest();
        User owner = testUser();
        owner.setDisabled(true);
        when(userService.getById(groupRequest.getOwnerId())).thenReturn(owner);

        assertThrows(DisabledObjectException.class, () -> groupServiceImpl.create(groupRequest, null));
    }

    @Test
    void testFindByOwner() {
        when(groupRepository.findByOwnerId(anyInt())).thenReturn(List.of(testGroup()));
        when(groupConverter.of(any(Group.class))).thenReturn(testGroupResponse());

        List<GroupResponse> actual = groupServiceImpl.findByOwner(1);

        assertEquals(1, actual.size());
        assertEquals(1, actual.get(0).getOwnerId());
        verify(groupRepository, times(1)).findByOwnerId(anyInt());
    }

    @Test
    void testFindGroupsWithClosedMaterial() {
        when(groupRepository.findGroupsWithClosedMaterial(1)).thenReturn(List.of(testGroup()));
        when(groupConverter.of(any(Group.class))).thenReturn(testGroupResponse());

        final List<GroupResponse> actual = groupServiceImpl.findGroupsWithClosedMaterial(1);

        assertEquals(1, actual.size());
        verify(groupRepository, times(1)).findGroupsWithClosedMaterial(anyInt());
    }

    @Test
    void testFindGroupsWithOpenedMaterial() {
        when(groupRepository.findGroupsWithOpenedMaterial(1)).thenReturn(List.of(testGroup()));
        when(groupConverter.of(any(Group.class))).thenReturn(testGroupResponse());

        List<GroupResponse> actual = groupServiceImpl.findGroupsWithOpenedMaterial(1);

        assertEquals(1, actual.size());
        verify(groupRepository, times(1)).findGroupsWithOpenedMaterial(anyInt());
    }

    @Test
    void testUpdateDisabled() {
        when(groupRepository.updateDisabled(anyInt(), anyBoolean())).thenReturn(1);

        groupServiceImpl.updateDisabled(1, true);

        verify(groupRepository, times(1)).updateDisabled(anyInt(), anyBoolean());
    }

    @Test
    void testUpdateDisabledThrowsNotFoundException() {
        when(groupRepository.updateDisabled(anyInt(), anyBoolean())).thenReturn(0);

        assertThrows(NotFoundException.class, () -> groupServiceImpl.updateDisabled(1, true));
    }

    @Test
    void testFindById() {
        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(testGroup()));
        when(groupConverter.of(any(Group.class))).thenReturn(testGroupResponse());

        GroupResponse actual = groupServiceImpl.findById(1);

        assertEquals("TestGroup", actual.getName());
        verify(groupRepository, times(1)).findById(anyInt());
    }

    @Test
    void testFindByIdThrowsNotFoundException() {
        when(groupRepository.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> groupServiceImpl.findById(1));
    }

    @Test
    void testReadByIdThrowsDisabledObjectException() {
        Group group = testGroup();
        group.setDisabled(true);
        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(group));

        assertThrows(DisabledObjectException.class, () -> groupServiceImpl.findById(1));
    }

    private GroupResponse testGroupResponse() {
        return GroupResponse.builder()
                .name("TestGroup")
                .ownerId(1)
                .disabled(false)
                .build();
    }

    private GroupRequest testGroupRequest() {
        return GroupRequest.builder()
                .name("TestGroup")
                .ownerId(1)
                .courseIds(Collections.emptySet())
                .build();
    }

    private Group testGroup() {
        return Group.builder()
                .name("TestGroup")
                .ownerId(1)
                .disabled(false)
                .courses(Collections.emptySet())
                .users(Collections.emptySet())
                .build();
    }

    private User testUser(){
        return User.builder()
                .name("TestUser")
                .disabled(false)
                .groups(Set.of(testGroup()))
                .build();
    }

    private Material testMaterial() {
        return Material.builder()
                .name("Material")
                .ownerId(1)
                .build();
    }

    private Course testCourse() {
        return Course.builder()
                .name("NewCourse")
                .ownerId(1)
                .disabled(false)
                .description("Description")
                .groups(Collections.emptySet())
                .materials(Collections.emptySet())
                .build();
    }

    private ChatRoom testChatRoom() {
        ChatRoom chatRoom = ChatRoom.builder()
                .messages(Collections.emptyList())
                .type(ChatRoom.ChatType.GROUP)
                .build();
        chatRoom.setId(1);
        return chatRoom;
    }
}
