package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.MaterialDto;
import com.softserve.itacademy.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping("/create")
    public ResponseEntity<MaterialDto> create(@RequestBody MaterialDto materialDto){
        return new ResponseEntity<>(materialService.create(materialDto), HttpStatus.CREATED);
    }
}
