package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.response.MaterialResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MaterialService {

    MaterialResponse findById(Integer id);
    MaterialResponse create(MaterialRequest materialRequest, MultipartFile file);
    Material getById(Integer id);
    DownloadFileResponse downloadById(Integer id);
    void delete(Integer id, Integer currentUserId);

}
