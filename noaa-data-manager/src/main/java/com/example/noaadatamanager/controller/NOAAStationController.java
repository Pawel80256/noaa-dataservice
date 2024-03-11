package com.example.noaadatamanager.controller;

import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.service.NOAAStationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam(name = "stationId") String stationId){
        noaaStationService.delete(stationId);
        return ResponseEntity.ok().build();
    }

}
