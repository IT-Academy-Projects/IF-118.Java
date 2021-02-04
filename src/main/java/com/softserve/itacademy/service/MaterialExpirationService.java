package com.softserve.itacademy.service;

import com.softserve.itacademy.request.MaterialExpirationRequest;
import com.softserve.itacademy.response.MaterialExpirationResponse;

import java.util.List;

public interface MaterialExpirationService {

    void setMaterialExpiration(MaterialExpirationRequest materialExpirationRequest);

    List<MaterialExpirationResponse> getMaterialExpiration(Integer materialId);
}
