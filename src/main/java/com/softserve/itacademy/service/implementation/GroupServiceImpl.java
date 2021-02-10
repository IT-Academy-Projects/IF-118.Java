package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.ChatRoom;
import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.Image;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.ImageRepository;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.response.GroupResponse;
import com.softserve.itacademy.service.ChatRoomService;
import com.softserve.itacademy.service.GroupService;
import com.softserve.itacademy.service.ImageService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.GroupConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    public static final String COURSE_ID_NOT_FOUND = "Course with such id was not found";
    private static final String GROUP_ID_NOT_FOUND = "Group with such id was not found";
    private final GroupRepository groupRepository;
    private final GroupConverter groupConverter;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final CourseRepository courseRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final MaterialRepository materialRepository;

    public GroupServiceImpl(GroupRepository groupRepository, GroupConverter groupConverter,
                            UserService userService, ChatRoomService chatRoomService,
                            CourseRepository courseRepository, ImageService imageService,
                            ImageRepository imageRepository, MaterialRepository materialRepository) {
        this.groupRepository = groupRepository;
        this.groupConverter = groupConverter;
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.courseRepository = courseRepository;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    @Transactional
    public GroupResponse create(GroupRequest groupRequest, MultipartFile file) {
        User owner = userService.getById(groupRequest.getOwnerId());

        if (owner.getDisabled()) {
            throw new DisabledObjectException("Group is disabled");
        }
        Set<Integer> courseIds = groupRequest.getCourseIds();
        Group newGroup;
        Set<Material> materials;
        if (courseIds == null) {
            newGroup = groupConverter.of(groupRequest, Collections.emptySet());
        } else {
            Set<Course> courses = courseRepository.findByIds(courseIds);
            newGroup = groupConverter.of(groupRequest, courses);
            materials = materialRepository.findByCourseIds(courseIds);
            newGroup.setMaterials(materials);
            courses.forEach(course -> course.getGroups().add(newGroup));
        }
        if (file != null) {
            Image image = new Image(imageService.compress(file));
            newGroup.setAvatar(imageRepository.save(image));
        }

        ChatRoom chat = chatRoomService.create();
        chat.setType(ChatRoom.ChatType.GROUP);
        newGroup.setChatRoom(chat);

        Group savedGroup = groupRepository.save(newGroup);
        return groupConverter.of(savedGroup);
    }

    @Override
    public List<GroupResponse> findAll() {
        return groupRepository.findAll().stream()
                .map(groupConverter::of).collect(Collectors.toList());
    }

    @Override
    public List<GroupResponse> findByOwner(Integer id) {
        return groupRepository.findByOwnerId(id).stream()
                .map(groupConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getAvatarById(Integer id) {
        if (!groupRepository.existsById(id)) {
            throw new NotFoundException(COURSE_ID_NOT_FOUND);
        }
        byte[] avatar = groupRepository.getAvatarById(id);
        if (avatar == null) {
            throw new NotFoundException("Avatar doesn't exist for this course");
        }
        return avatar;
    }

    @Override
    public void updateGroup(Integer groupId, GroupRequest groupRequest) {
        Group group = groupRepository.findByIdAndOwnerId(groupId, groupRequest.getOwnerId())
                .orElseThrow(() -> new NotFoundException(GROUP_ID_NOT_FOUND));
        group.setName(groupRequest.getName());
    }

    @Override
    public List<GroupResponse> findGroupsWithClosedMaterial(Integer materialId) {
        List<Group> groups = groupRepository.findGroupsWithClosedMaterial(materialId);
        if (groups.isEmpty()) {
            return Collections.emptyList();
        }
        return groups.stream().map(groupConverter::of).collect(Collectors.toList());
    }

    @Override
    public void submitAssignment(Integer groupId, Integer assignmentId) {
        groupRepository.submitAssignment(groupId, assignmentId);
    }

    @Override
    public Set<Integer> findAllUsersIds(Group group) {
        return groupRepository.findAllById(group.getId());
    }

    @Override
    public void updateDisabled(Integer id, boolean disabled) {
        if (groupRepository.updateDisabled(id, disabled) == 0) {
            throw new NotFoundException(GROUP_ID_NOT_FOUND);
        }
    }

    @Override
    public GroupResponse findById(Integer id) {
        Group group = getById(id);
        if (group.getDisabled()) {
            throw new DisabledObjectException("Group is disabled");
        }
        return groupConverter.of(group);
    }

    private Group getById(Integer id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(GROUP_ID_NOT_FOUND));
    }
}
