package com.softserve.itacademy.dto;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Material;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MaterialDto {

    private Integer id;
    private String name;
    private Course course;

    public static MaterialDto create(Material material) {
        return MaterialDto.builder()
                .id(material.getId())
                .name(material.getName())
                .course(material.getCourse()).build();
    }

    public static Material convertToEntity(MaterialDto materialDto){
        Material material = new Material();
        material.setCourse(materialDto.getCourse());
        material.setName(materialDto.getName());
        return material;
    }
}
