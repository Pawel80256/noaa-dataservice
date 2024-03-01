package com.master.dataloader.controller;

import com.master.dataloader.dtos.NOAAStationDto;
import com.master.dataloader.service.NOAAStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("NOAA/station")
public class NOAAStationController {

    private final NOAAStationService noaaStationService;

    public NOAAStationController(NOAAStationService noaaStationService) {
        this.noaaStationService = noaaStationService;
    }

    @GetMapping
    public ResponseEntity<List<NOAAStationDto>> getAll(){
        return ResponseEntity.ok(noaaStationService.getAll());
    }

    @GetMapping ("/remote")
    ResponseEntity<List<NOAAStationDto>> getAllRemote(@RequestParam(name = "locationId") String locationId) throws Exception {
        return ResponseEntity.ok(noaaStationService.getAllRemoteDtos(locationId));
    }

    @PutMapping("/load")
    ResponseEntity<Void> loadByIds(
            @RequestParam(name = "locationId") String locationId,
            @RequestBody List<String> stationIds
    ) throws Exception {
        noaaStationService.loadByIds(locationId,stationIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> stationIds){
        noaaStationService.deleteByIds(stationIds);
        return ResponseEntity.ok().build();
    }
}
