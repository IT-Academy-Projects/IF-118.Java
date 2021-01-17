package com.softserve.itacademy.service.s3;

import com.softserve.itacademy.exception.FileHasNoExtensionException;
import com.softserve.itacademy.exception.FileProcessingException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class S3Utils {

    private final AmazonS3ClientService amazonS3ClientService;

    public S3Utils(AmazonS3ClientService amazonS3ClientService) {
        this.amazonS3ClientService = amazonS3ClientService;
    }

    public byte[] downloadFile(String fileReference, String bucketName, String folderName) {
        byte[] bytes;
        try {
            bytes = amazonS3ClientService.download(bucketName, folderName + "/" + fileReference);
        } catch (IOException e) {
            throw new FileProcessingException("Cannot read file");
        }
        return bytes;
    }

    public String saveFile(MultipartFile file, String bucketName, String folderName) {
//        TODO the same ...
        String[] split = file.getOriginalFilename().split("\\.");
        if (split.length < 1) { throw new FileHasNoExtensionException("Wrong file format"); }
        String extension = split[split.length - 1];
        String fileReference = UUID.randomUUID().toString().toLowerCase() + "." + extension;
        try {
            amazonS3ClientService.upload(bucketName,  folderName + "/" + fileReference, file.getInputStream());
        } catch (IOException e) {
            throw new FileProcessingException("Cannot write file");
        }
        return fileReference;
    }

    public void delete(String bucketName, String fileReference) {
        amazonS3ClientService.delete(bucketName, fileReference);
    }
}
