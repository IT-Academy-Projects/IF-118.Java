package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.MaterialResponse;

public interface MaterialService {

    MaterialResponse findById(Integer id);
    MaterialResponse create(MaterialRequest materialRequest);
    Material getById(Integer id);

}
