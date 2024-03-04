package com.example.noaadatamanager.controller;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.service.NOAADataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("DATA_MANAGER/measurements")
public class NOAADataController {
    private final NOAADataService noaaDataService;

    public NOAADataController(NOAADataService noaaDataService) {
        this.noaaDataService = noaaDataService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody MeasurementInputDto measurementInputDto){
        noaaDataService.create(measurementInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
