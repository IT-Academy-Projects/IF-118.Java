package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.response.MaterialResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MaterialConverter {

    private final ModelMapper mapper;

    public MaterialResponse of(Material material) {
        MaterialResponse map = mapper.map(material, MaterialResponse.class);
        return map;
    }

}
