package com.example.noaadatamanager.controller;

import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.service.NOAAStationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stations")
public class NOAAStationController {
    private final NOAAStationService noaaStationService;

    public NOAAStationController(NOAAStationService noaaStationService) {
        this.noaaStationService = noaaStationService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody StationInputDto stationInputDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(noaaStationService.create(stationInputDto));
    }

}
