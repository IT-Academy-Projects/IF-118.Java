package com.softserve.itacademy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.service.MaterialService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/materials")
public class MaterialController {

    private MaterialService materialService;
    private ObjectMapper objectMapper;

    public MaterialController(MaterialService materialService, ObjectMapper objectMapper) {
        this.materialService = materialService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<MaterialResponse> create(@RequestPart(value = "material") String data,
                                                   @RequestPart(value = "file") MultipartFile file) throws JsonProcessingException {
        MaterialRequest materialRequest = objectMapper.readValue(data, MaterialRequest.class);
        return new ResponseEntity<>(materialService.create(materialRequest, file), HttpStatus.CREATED);
    }

//    @PostMapping("/{id}/file")
//    public ResponseEntity<Resource> download(@PathVariable Integer id) {
//        return new ResponseEntity<>(new ByteArrayResource(materialService.downloadFile("")), HttpStatus.OK);
//    }

}
