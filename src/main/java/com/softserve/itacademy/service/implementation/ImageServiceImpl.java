package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.exception.FileProcessingException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.ImageRepository;
import com.softserve.itacademy.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    private static final int TARGET_IMAGE_SIZE = 280;

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public byte[] findImageById(Integer id) {
        if (!imageRepository.existsById(id)){
            throw new NotFoundException("Resource not found");
        }
        return imageRepository.findFileById(id);
    }

    public byte[] compress(MultipartFile file) {
        try {

            ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes());
            BufferedImage originalImage = ImageIO.read(inputStream);
            inputStream.close();

            java.awt.Image resultingImage = originalImage.getScaledInstance(TARGET_IMAGE_SIZE, TARGET_IMAGE_SIZE, java.awt.Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage(TARGET_IMAGE_SIZE, TARGET_IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(outputImage, "png", byteArrayOutputStream);
            byte[] imageByteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();

            return imageByteArray;
        } catch (IOException e) {
            throw new FileProcessingException("Cannot compress image");
        }
    }

}
