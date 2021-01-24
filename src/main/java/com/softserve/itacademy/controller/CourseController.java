package com.softserve.itacademy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.request.DescriptionRequest;
import com.softserve.itacademy.request.DisableRequest;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.security.perms.CourseCreatePermission;
import com.softserve.itacademy.security.perms.CourseDeletePermission;
import com.softserve.itacademy.security.perms.CourseReadPermission;
import com.softserve.itacademy.security.perms.CourseUpdatePermission;
import com.softserve.itacademy.security.perms.roles.AdminRolePermission;
import com.softserve.itacademy.service.CourseService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;
    private final ObjectMapper objectMapper;

    public CourseController(CourseService courseService, ObjectMapper objectMapper) {
        this.courseService = courseService;
        this.objectMapper = objectMapper;
    }

    @CourseCreatePermission
    @PostMapping
    public ResponseEntity<CourseResponse> create(@RequestPart(value = "course") String course,
                                                 @RequestPart(value = "file",  required = false) MultipartFile file,
                                                 @AuthenticationPrincipal User currentUser) throws JsonProcessingException {
        CourseRequest courseRequest = objectMapper.readValue(course, CourseRequest.class);
        courseRequest.setOwnerId(currentUser.getId());
        return new ResponseEntity<>(courseService.create(courseRequest, file), HttpStatus.CREATED);
    }

    @CourseReadPermission
    @GetMapping(path = "/{id}/avatar", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public ResponseEntity<Resource> downloadAvatarById(@PathVariable Integer id) {
        HttpHeaders headers = new HttpHeaders();
        byte[] avatar = courseService.getAvatarById(id);
        headers.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).getHeaderValue());
        return new ResponseEntity<>(new ByteArrayResource(avatar), headers, HttpStatus.OK);
    }

    @CourseReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(courseService.readById(id), HttpStatus.OK);
    }

    @CourseReadPermission
    @GetMapping
    public ResponseEntity<List<CourseResponse>> findByOwner(@AuthenticationPrincipal User currentUser) {
        return new ResponseEntity<>(courseService.findByOwner(currentUser.getId()), HttpStatus.OK);
    }

    @AdminRolePermission
    @GetMapping("/forAdmin")
    public ResponseEntity<List<CourseResponse>> findAll() {
        return new ResponseEntity<>(courseService.findAll(), HttpStatus.OK);
    }

    @CourseDeletePermission
    @PatchMapping("/{id}/disabled")
    public ResponseEntity<Void> updateDisabled(@PathVariable Integer id, @RequestBody DisableRequest disabledRequest) {
        courseService.updateDisabled(id, disabledRequest.isDisabled());
        return new ResponseEntity<>(OK);
    }

    @CourseUpdatePermission
    @PatchMapping("/{id}/description")
    public ResponseEntity<Void> updateDescription(@PathVariable Integer id, @RequestBody DescriptionRequest descriptionRequest) {
        courseService.updateDescription(id, descriptionRequest.getDescription());
        return new ResponseEntity<>(OK);
    }
}
