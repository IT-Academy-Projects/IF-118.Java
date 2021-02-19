package com.softserve.itacademy.controller;

import com.softserve.itacademy.service.implementation.ImageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ImageController.class })
@AutoConfigureMockMvc
@WebMvcTest
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageServiceImpl imageService;

    @Test
    @WithMockUser
    void testDownloadAvatarById() throws Exception {
        when(imageService.findImageById(anyInt())).thenReturn(new byte[]{0, 1});
        mockMvc.perform(get("/api/v1/images/1")
                .accept(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
                .andReturn();
        verify(imageService, times(1)).findImageById(1);
    }

}