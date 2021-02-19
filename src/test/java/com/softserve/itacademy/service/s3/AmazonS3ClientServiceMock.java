package com.softserve.itacademy.service.s3;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("test")
public class AmazonS3ClientServiceMock implements AmazonS3ClientService {

    @Override
    public String upload(String bucketName, String folder, MultipartFile file) {
        return null;
    }

    @Override
    public byte[] download(String bucketName, String fileReference) {
        return new byte[0];
    }

    @Override
    public void delete(String bucketName, String folder, String fileReference) {

    }
}
