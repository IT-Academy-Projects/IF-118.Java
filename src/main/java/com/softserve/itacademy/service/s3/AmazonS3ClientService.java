package com.softserve.itacademy.service.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.softserve.itacademy.exception.FileProcessingException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Service
public class AmazonS3ClientService {

    @Value("${S3_ACCESSKEY}")
    private String accessKey;

    @Value("${S3_SECRETKEY}")
    private String secretKey;

    private AmazonS3 s3client;

    /**
     * The method for authorization into AWS
     * It gives s3 client for working with service
     **/
    @PostConstruct
    private void connectClient() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    public String upload(String bucketName, String folder, MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileReference = folder + "/" + UUID.randomUUID().toString().toLowerCase() + "." + extension;
        try {
            s3client.putObject(bucketName, fileReference, file.getInputStream(), new ObjectMetadata());
        } catch (IOException e) {
            throw new FileProcessingException("Cannot write file");
        }
        return fileReference;
    }

    public byte[] download(String bucketName, String fileReference) {
        S3Object s3object = s3client.getObject(bucketName, fileReference);
        try {
            return s3object.getObjectContent().readAllBytes();
        } catch (IOException e) {
            throw new FileProcessingException("Cannot read bytes from file");
        }
    }

    public void delete(String bucketName, String folder, String fileReference) {
        s3client.deleteObject(bucketName, folder + "/" + fileReference);
    }

}
