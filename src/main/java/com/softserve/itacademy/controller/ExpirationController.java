package com.softserve.itacademy.controller;

import com.softserve.itacademy.request.MaterialExpirationRequest;
import com.softserve.itacademy.response.MaterialExpirationResponse;
import com.softserve.itacademy.service.MaterialExpirationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.softserve.itacademy.config.Constance.API_V1;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(API_V1 + "expirations")
public class ExpirationController {

    private final MaterialExpirationService materialExpirationService;

    public ExpirationController(MaterialExpirationService materialExpirationService) {
        this.materialExpirationService = materialExpirationService;
    }

    @GetMapping("/{materialId}")
    public ResponseEntity<List<MaterialExpirationResponse>> read(@PathVariable Integer materialId) {
        return new ResponseEntity<>(materialExpirationService.getMaterialExpiration(materialId), OK);
    }

    @PostMapping("/")
    public ResponseEntity<Void> create( @RequestBody MaterialExpirationRequest materialExpirationRequest) {
        materialExpirationService.setMaterialExpiration(materialExpirationRequest);
        return new ResponseEntity<>(OK);
    }
}
