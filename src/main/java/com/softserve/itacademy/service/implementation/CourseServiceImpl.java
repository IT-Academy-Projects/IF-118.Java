package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.service.CourseService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.CourseConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserService userService;
    private final CourseConverter courseConverter;
    private final MaterialRepository materialRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserService userService, CourseConverter courseConverter, MaterialRepository materialRepository) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.courseConverter = courseConverter;
        this.materialRepository = materialRepository;
    }

    @Override
    public CourseResponse create(CourseRequest courseRequest) {
        log.info("Creating course from CourseRequest with name: {}, description: {}, ownerId: {}", courseRequest.getName(),courseRequest.getDescription(), courseRequest.getOwnerId());
        userService.findById(courseRequest.getOwnerId());
        Set<Material> materials = Collections.emptySet();
        Set<Integer> materialIds = courseRequest.getMaterialIds();
        if (materialIds != null) {
            String query = materialIds.stream().map(Object::toString).collect(Collectors.joining(", "));
            materials = materialRepository.findByIds(query);
            log.info("Selected materials with ids {}", query);
        }

        Course course = courseConverter.of(courseRequest, materials);
        Course savedCourse = courseRepository.save(course);
        log.info("Created course {}", savedCourse);
        return courseConverter.of(savedCourse);
    }

    @Override
    public List<CourseResponse> findAll() {
        log.info("Searching for courses...");
        return courseRepository.findAll().stream()
                .map(courseConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> findByOwner(Integer id) {
        log.info("Searching courses for user {}", id);
        List<Course> coursesByOwner = courseRepository.findByOwner(id);
        if (coursesByOwner == null) {
            return Collections.emptyList();
        }
        return coursesByOwner.stream()
                .map(courseConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public void updateDisabled(Integer id, boolean disabled) {
        if (courseRepository.updateDisabled(id, disabled) == 0) {
            throw new NotFoundException("Course with such id was not found");
        }
    }

    @Override
    public void updateDescription(Integer id, String description) {
        if (courseRepository.updateDescription(id, description) == 0) {
            throw new NotFoundException("Course was not found");
        }
    }

    @Override
    public CourseResponse readById(Integer id) {
        return courseConverter.of(getById(id));
    }

    @Override
    public CourseResponse update(CourseRequest courseDto) {
        return null;
    }

    public Course getById(Integer id) {
        return courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course with such id was not found"));
    }

}
