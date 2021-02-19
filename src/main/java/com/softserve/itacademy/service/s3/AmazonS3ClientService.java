package com.softserve.itacademy.service.s3;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3ClientService {
    String upload(String bucketName, String folder, MultipartFile file);

    byte[] download(String bucketName, String fileReference);

    void delete(String bucketName, String folder, String fileReference);
}
