package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    byte[] compress(MultipartFile file);
    Image save(byte[] file);
    byte[] findImageById(Integer id);

}
