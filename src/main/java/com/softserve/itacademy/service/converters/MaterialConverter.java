package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.response.statistic.MaterialStatisticResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MaterialConverter {

    private final ModelMapper mapper;

    public MaterialResponse of(Material material) {
        return mapper.map(material, MaterialResponse.class);
    }

    public MaterialStatisticResponse statisticOf(Material material) {

        return mapper.map(material, MaterialStatisticResponse.class);
    }

}
