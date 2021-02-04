package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.ChatRoom;
import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.Image;
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
import com.softserve.itacademy.service.ExpirationService;
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

import static com.softserve.itacademy.config.Constance.COURSE_ID_NOT_FOUND;
import static com.softserve.itacademy.config.Constance.GROUP_ID_NOT_FOUND;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupConverter groupConverter;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final CourseRepository courseRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final MaterialRepository materialRepository;
    private final ExpirationService expirationService;

    public GroupServiceImpl(GroupRepository groupRepository, GroupConverter groupConverter, UserService userService, ChatRoomService chatRoomService, CourseRepository courseRepository, ImageService imageService, ImageRepository imageRepository, MaterialRepository materialRepository, ExpirationService expirationService) {
        this.groupRepository = groupRepository;
        this.groupConverter = groupConverter;
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.courseRepository = courseRepository;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.materialRepository = materialRepository;
        this.expirationService = expirationService;
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
//        Set<Integer> materialIds = null;
        if (courseIds == null) {
            newGroup = groupConverter.of(groupRequest, Collections.emptySet());
        } else {
            Set<Course> courses = courseRepository.findByIds(courseIds);
            newGroup = groupConverter.of(groupRequest, courses);
            courses.forEach(course -> course.getGroups().add(newGroup));
//            materialIds = materialRepository.findByCourseIds(courseIds);
        }
        if (file != null) {
            Image image = new Image(imageService.compress(file));
            newGroup.setAvatar(imageRepository.save(image));
        }

        ChatRoom chat = chatRoomService.create();
        chat.setType(ChatRoom.ChatType.GROUP);
        newGroup.setChatRoom(chat);

        Group savedGroup = groupRepository.save(newGroup);
//        if (materialIds != null) {
//            expirationService.create();
//            materialIds.forEach(id -> materialRepository.saveMaterialsGroups(id, savedGroup.getId()));
//        }
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
        List<Group> groupsByCourseId = groupRepository.findGroupsWithClosedMaterial(materialId);
        if (groupsByCourseId.isEmpty()) {
            return Collections.emptyList();
        }
        return groupsByCourseId.stream().map(groupConverter::of).collect(Collectors.toList());
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
