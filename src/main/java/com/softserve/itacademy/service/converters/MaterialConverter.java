package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.response.statistic.MaterialStatisticResponse;
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

    public MaterialStatisticResponse statisticOf(Material material) {
        MaterialStatisticResponse map = mapper.map(material, MaterialStatisticResponse.class);
        map.setAssignmentStatisticResponses(material.getAssignments().stream()
        .map(assignmentConverter::statisticOf)
                .collect(Collectors.toSet()));
        return map;
    }

}
