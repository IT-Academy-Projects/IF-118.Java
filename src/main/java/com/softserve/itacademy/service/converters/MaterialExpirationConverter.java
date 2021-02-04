package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.MaterialExpiration;
import com.softserve.itacademy.response.MaterialExpirationResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MaterialExpirationConverter {
    private final ModelMapper mapper;

    public MaterialExpirationResponse of(MaterialExpiration materialExpiration) {
        MaterialExpirationResponse response = mapper.map(materialExpiration, MaterialExpirationResponse.class);
        response.setMaterialId(materialExpiration.getMaterial().getId());
        response.setGroupId(materialExpiration.getGroup().getId());
        return response;
    }
}
