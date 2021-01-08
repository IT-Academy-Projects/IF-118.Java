package com.softserve.itacademy.service.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

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

    public void upload(String bucketName, String filename, InputStream inputStream) {
        s3client.putObject(bucketName, filename, inputStream, new ObjectMetadata());
    }

    public byte[] download(String bucketName, String fileReference) throws IOException {
        S3Object s3object = s3client.getObject(bucketName, fileReference);
        return s3object.getObjectContent().readAllBytes();
    }

}
