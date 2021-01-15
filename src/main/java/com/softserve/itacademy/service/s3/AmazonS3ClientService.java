package com.softserve.itacademy.service.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.softserve.itacademy.exception.FileHasNoExtensionException;
import com.softserve.itacademy.exception.FileProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

import static com.softserve.itacademy.service.s3.S3Constants.BUCKET_NAME;
import static com.softserve.itacademy.service.s3.S3Constants.MATERIALS_FOLDER;

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
    public void connectClient() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    public String saveFile(MultipartFile file) {
        String[] split = file.getOriginalFilename().split("\\.");
        if (split.length < 1) { throw new FileHasNoExtensionException("Wrong file format"); }
        String extension = split[split.length - 1];
        String fileReference = UUID.randomUUID().toString().toLowerCase() + "." + extension;
        try {
            s3client.putObject(BUCKET_NAME,  MATERIALS_FOLDER + "/" + fileReference, file.getInputStream(),
                    new ObjectMetadata());
        } catch (IOException e) {
            throw new FileProcessingException("Cannot write file");
        }
        return fileReference;
    }

    public byte[] downloadFile(String fileReference) {
        byte[] bytes;
        try {
            bytes = s3client.getObject(BUCKET_NAME, MATERIALS_FOLDER + "/" + fileReference)
                    .getObjectContent().readAllBytes();
        } catch (IOException e) {
            throw new FileProcessingException("Cannot read file");
        }
        return bytes;
    }
}
