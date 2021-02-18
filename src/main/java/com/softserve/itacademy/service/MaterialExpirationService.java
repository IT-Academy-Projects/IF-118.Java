package com.softserve.itacademy.service;

import com.softserve.itacademy.request.MaterialExpirationRequest;
import com.softserve.itacademy.response.MaterialExpirationResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface MaterialExpirationService {

    void setMaterialExpiration(MaterialExpirationRequest materialExpirationRequest);

    List<MaterialExpirationResponse> getMaterialExpiration(Integer materialId);

    void updateMaterialExpiration(Integer expirationId, LocalDateTime expirationDate);

    List<MaterialExpirationResponse> findAllExpiringBy(LocalDateTime dateTime);

    int deleteByExpirationDate();
}
