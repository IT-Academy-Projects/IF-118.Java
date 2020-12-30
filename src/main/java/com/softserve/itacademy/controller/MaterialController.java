package com.softserve.itacademy.controller;

import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.service.MaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/materials")
public class MaterialController {

    private MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping
    public ResponseEntity<MaterialResponse> create(@RequestBody MaterialRequest materialRequest) {
        return new ResponseEntity<>(materialService.create(materialRequest), HttpStatus.CREATED);
    }

}
