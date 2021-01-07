package com.softserve.itacademy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.service.MaterialService;
import javassist.bytecode.ByteArray;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponse> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(materialService.findById(id), HttpStatus.OK);
    }

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

}
