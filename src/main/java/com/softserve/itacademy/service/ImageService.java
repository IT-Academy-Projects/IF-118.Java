package com.softserve.itacademy.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    byte[] compress(MultipartFile file);
    byte[] findImageById(Integer id);

}
