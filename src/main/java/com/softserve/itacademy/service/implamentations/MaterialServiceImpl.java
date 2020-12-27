package com.softserve.itacademy.service.implamentations;

import com.softserve.itacademy.dto.MaterialDto;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialServiceImpl implements MaterialService {

    private MaterialRepository materialRepository;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Override
    public MaterialDto create(MaterialDto materialDto) {
        Material material = MaterialDto.convertToEntity(materialDto);
        return MaterialDto.create(materialRepository.save(material));
    }
}
