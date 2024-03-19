package com.example.noaadatamanager.controller;

import com.example.noaadatamanager.annotations.RequestAuthorization;
import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.service.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    @RequestAuthorization(roles = {"ADMIN","STATION_MANAGER"})
    public ResponseEntity<String> create(@RequestBody StationInputDto stationInputDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stationService.create(stationInputDto));
    }

    @DeleteMapping
    @RequestAuthorization(roles = {"ADMIN","STATION_MANAGER"})
    public ResponseEntity<Void> delete(@RequestParam(name = "stationId") String stationId){
        stationService.delete(stationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/name")
    @RequestAuthorization(roles = {"ADMIN","STATION_MANAGER"})
    public ResponseEntity<Void> updateName(
            @RequestParam(name = "stationId") String stationId,
            @RequestParam(name = "newName") String newName
    ){
        stationService.updateName(stationId, newName);
        return ResponseEntity.ok().build();
    }
}
