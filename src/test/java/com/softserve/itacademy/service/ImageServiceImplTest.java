package com.softserve.itacademy.service;

import com.softserve.itacademy.exception.FileProcessingException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.ImageRepository;
import com.softserve.itacademy.service.implementation.ImageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ImageServiceImplTest {

    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private ImageRepository imageRepository;

    @Test
    void testFindImageById() {
        byte[] expectedFile = new byte[1];
        when(imageRepository.existsById(anyInt())).thenReturn(true);
        when(imageRepository.findFileById(anyInt())).thenReturn(expectedFile);
        byte[] file = imageService.findImageById(1);
        assertEquals(file, expectedFile);
        verify(imageRepository, times(1)).existsById(1);
        verify(imageRepository, times(1)).findFileById(1);
        verifyNoMoreInteractions(imageRepository);
    }

    @Test()
    void testFindImageByIdNotFound() {
        when(imageRepository.existsById(anyInt())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> imageService.findImageById(1));
        verify(imageRepository, times(1)).existsById(1);
        verify(imageRepository, never()).findFileById(1);
        verifyNoMoreInteractions(imageRepository);
    }

    @Test
    void testCompressWithWrongMediaType() throws IOException {
        Path path = Paths.get("src/test/resources/image.png");
        MultipartFile file = new MockMultipartFile("image.png",
                "image.png", MediaType.TEXT_HTML_VALUE, Files.readAllBytes(path));
        assertThrows(FileProcessingException.class, () -> imageService.compress(file));
    }

    @Test
    void testCompress() throws IOException {
        Path path = Paths.get("src/test/resources/image.png");
        byte[] fileBytes = Files.readAllBytes(path);
        MultipartFile file = new MockMultipartFile("image.png",
                "image.png", MediaType.IMAGE_PNG_VALUE, fileBytes);
        assertTrue(fileBytes.length > imageService.compress(file).length);
    }

}