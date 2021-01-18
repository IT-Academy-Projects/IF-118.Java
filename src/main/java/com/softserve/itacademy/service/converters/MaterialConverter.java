package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.response.MaterialResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class MaterialConverter {

    private final ModelMapper mapper;
    private final AssignmentConverter assignmentConverter;

    public MaterialResponse of(Material material) {
        MaterialResponse map = mapper.map(material, MaterialResponse.class);
        if (material.getAssignments() != null) {
            map.setAssignments(material.getAssignments().stream()
                    .map(assignmentConverter::of)
                    .collect(Collectors.toSet()));
        }
        return map;
    }

}
