package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.service.CourseService;
import com.softserve.itacademy.service.MaterialService;
import com.softserve.itacademy.service.converters.MaterialConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialServiceImpl implements MaterialService {

    private MaterialRepository materialRepository;
    private CourseService courseService;
    private MaterialConverter materialConverter;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository, MaterialConverter materialConverter, CourseService courseService) {
        this.materialRepository = materialRepository;
        this.materialConverter = materialConverter;
        this.courseService = courseService;
    }


    @Override
    public MaterialResponse findById(Integer id) {
        return materialConverter.of(getById(id));
    }

    @Override
    public MaterialResponse create(MaterialRequest materialRequest) {
        Course course = courseService.getById(materialRequest.getCourseId());

        if (course.getDisabled()) {
            throw new DisabledObjectException();
        }

        Material material = Material.builder()
                .name(materialRequest.getName())
                .course(course)
                .build();
        material = materialRepository.save(material);
        return materialConverter.of(material);
    }

    public Material getById(Integer id) {
        return materialRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
