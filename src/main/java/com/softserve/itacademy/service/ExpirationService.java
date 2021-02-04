package com.softserve.itacademy.service;

import com.softserve.itacademy.request.MaterialExpirationRequest;
import com.softserve.itacademy.response.MaterialExpirationResponse;

public interface ExpirationService {

    void setMaterialExpiration(MaterialExpirationRequest materialExpirationRequest);

    MaterialExpirationResponse getMaterialExpiration(Integer materialId);
}
