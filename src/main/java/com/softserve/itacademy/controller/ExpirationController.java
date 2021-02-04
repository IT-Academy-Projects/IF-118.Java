package com.softserve.itacademy.controller;

import com.softserve.itacademy.request.MaterialExpirationRequest;
import com.softserve.itacademy.response.MaterialExpirationResponse;
import com.softserve.itacademy.service.ExpirationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.softserve.itacademy.config.Constance.API_V1;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(API_V1 + "expiration")
public class ExpirationController {

    private final ExpirationService expirationService;

    public ExpirationController(ExpirationService expirationService) {
        this.expirationService = expirationService;
    }

    @GetMapping("/{materialId}")
    public ResponseEntity<MaterialExpirationResponse> getMaterialExpiration(@PathVariable Integer materialId) {
        return new ResponseEntity<>(expirationService.getMaterialExpiration(materialId), OK);
    }

    @PatchMapping("/{materialId}")
    public ResponseEntity<Void> setMaterialExpiration(@PathVariable Integer materialId, @RequestBody MaterialExpirationRequest materialExpirationRequest) {
        expirationService.setMaterialExpiration(materialExpirationRequest);
        return new ResponseEntity<>(OK);
    }
}
