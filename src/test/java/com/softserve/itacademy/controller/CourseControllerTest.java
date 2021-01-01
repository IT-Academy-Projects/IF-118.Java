package com.softserve.itacademy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.config.ErrorConfigurationProperties;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.request.ErrorRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.service.CourseService;
import liquibase.pro.packaged.O;
import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ErrorConfigurationProperties errorConfigurationProperties;
    @MockBean
    private CourseService courseService;

    @Test
    void create_whenValidRequest_thenCreateCourse() throws Exception {
        CourseResponse courseDto = getCourseDto();
        when(courseService.create(any(CourseRequest.class))).thenReturn(courseDto);

        ResultActions newCourse = this.mockMvc.perform(post("/api/v1/courses/create")
                .content(asJsonString(courseDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("NewCourse"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readValue()
        verify(courseService, times(1)).create(any());
    }

    @Test
    void create_whenInValidRequest_thenThrowNotFounfExeption() throws Exception {
        when(courseService.create(any(CourseRequest.class))).thenThrow(NotFoundException.class);
        when(errorConfigurationProperties.getExceptions()).thenReturn(Map.of("NOT_FOUND", new ErrorRequest("Such entity not found")));

        this.mockMvc.perform(post("/api/v1/courses/create")
                .content(asJsonString(getCourseDto()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Such entity not found")));
        verify(courseService, times(1)).create(any());
        verify(errorConfigurationProperties, times(1)).getExceptions();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CourseResponse getCourseDto() {
        return CourseResponse.builder().name("NewCourse").build();
    }
}
