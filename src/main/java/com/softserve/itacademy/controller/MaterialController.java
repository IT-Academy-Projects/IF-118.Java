package com.softserve.itacademy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.security.perms.CourseCreatePermission;
import com.softserve.itacademy.security.perms.CourseDeletePermission;
import com.softserve.itacademy.security.perms.CourseReadPermission;
import com.softserve.itacademy.service.MaterialService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static com.softserve.itacademy.config.Constance.API_V1;

@RestController
@RequestMapping(API_V1 + "materials")
public class MaterialController {

    private final MaterialService materialService;
    private final ObjectMapper objectMapper;

    public MaterialController(MaterialService materialService, ObjectMapper objectMapper) {
        this.materialService = materialService;
        this.objectMapper = objectMapper;
    }

    @CourseCreatePermission
    @PostMapping
    public ResponseEntity<MaterialResponse> create(@RequestPart(value = "material") String data,
                                                   @RequestPart(value = "file") MultipartFile file) throws JsonProcessingException {
        MaterialRequest materialRequest = objectMapper.readValue(data, MaterialRequest.class);
        return new ResponseEntity<>(materialService.create(materialRequest, file), HttpStatus.CREATED);
    }

    @CourseReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponse> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(materialService.findById(id), HttpStatus.OK);
    }

    @CourseReadPermission
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> downloadById(@PathVariable Integer id) {
        DownloadFileResponse downloadFileResponse = materialService.downloadById(id);

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(downloadFileResponse.getFileName(), StandardCharsets.UTF_8)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA)
                .headers(headers)
                .body(downloadFileResponse.getFile());
    }

    @CourseDeletePermission
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User currentUser, @PathVariable Integer id) {
        materialService.delete(id, currentUser.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
