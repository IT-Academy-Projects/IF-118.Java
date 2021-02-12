package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.entity.MaterialExpiration;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.MaterialExpirationRepository;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.MaterialExpirationRequest;
import com.softserve.itacademy.response.MaterialExpirationResponse;
import com.softserve.itacademy.service.converters.MaterialExpirationConverter;
import com.softserve.itacademy.service.implementation.MaterialExpirationServiceImpl;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

public class MaterialExpirationServiceImplTest {
    @Mock
    private MaterialRepository materialRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private MaterialExpirationRepository materialExpirationRepository;
    @Mock
    private MaterialExpirationConverter materialExpirationConverter;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private MaterialExpirationServiceImpl materialExpirationServiceImpl;

    @BeforeEach
    void initialize() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSetMaterialExpiration() {
        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(generateGroup()));
        when(materialRepository.findById(anyInt())).thenReturn(Optional.of(generateMaterial()));
        when(materialExpirationConverter.of(generateMaterialExpirationRequest(),
                generateGroup(), generateMaterial())).thenReturn(generateMaterialExpiration());
        when(materialExpirationRepository.save(any(MaterialExpiration.class))).thenReturn(generateMaterialExpiration());

        materialExpirationServiceImpl.setMaterialExpiration(generateMaterialExpirationRequest());

        verify(materialExpirationRepository, times(1)).save(any());

    }
    @Test
    void testSetMaterialExpirationThrowsGroupNotFoundException() {
        when(groupRepository.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> materialExpirationServiceImpl.setMaterialExpiration(generateMaterialExpirationRequest()));
        verify(groupRepository, times(1)).findById(anyInt());
    }
    @Test
    void testSetMaterialExpirationThrowsMaterialNotFoundException() {
        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(generateGroup()));
        when(materialRepository.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> materialExpirationServiceImpl.setMaterialExpiration(generateMaterialExpirationRequest()));
        verify(materialRepository, times(1)).findById(anyInt());
    }


    private Group generateGroup() {
        return Group.builder()
                .name("Group")
                .ownerId(1)
                .build();
    }

    private Material generateMaterial() {
        return Material.builder()
                .name("Material")
                .ownerId(1)
                .build();
    }

    private MaterialExpirationRequest generateMaterialExpirationRequest() {
        return MaterialExpirationRequest.builder()
                .materialId(1)
                .groupId(1)
                .expirationDate(LocalDateTime.of(2020, 1, 1, 0, 0))
                .startDate(LocalDateTime.now())
                .build();
    }

    private MaterialExpirationResponse generateMaterialExpirationResponse() {
        return MaterialExpirationResponse.builder()
                .materialId(1)
                .groupId(1)
                .expirationDate(LocalDateTime.of(2020, 1, 1, 0, 0))
                .startDate(LocalDateTime.now())
                .build();
    }

    private MaterialExpiration generateMaterialExpiration() {
        MaterialExpiration materialExpiration = MaterialExpiration.builder()
                .material(generateMaterial())
                .group(generateGroup())
                .build();
        materialExpiration.setExpirationDate(LocalDateTime.of(2022, 1, 1, 0, 0));
        materialExpiration.setStartDate(LocalDateTime.now());
        return materialExpiration;
    }
}
