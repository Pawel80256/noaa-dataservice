package com.master.dataloader.controller;

import com.master.dataloader.dtos.StationDto;
import com.master.dataloader.service.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("NOAA/station")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity<List<StationDto>> getAll(){
        return ResponseEntity.ok(stationService.getAll());
    }

    @GetMapping ("/remote")
    ResponseEntity<List<StationDto>> getAllRemote(@RequestParam(name = "locationId") String locationId) throws Exception {
        return ResponseEntity.ok(stationService.getAllRemoteDtos(locationId));
    }

    @PutMapping("/load")
    ResponseEntity<Void> loadByIds(
            @RequestParam(name = "locationId") String locationId,
            @RequestBody List<String> stationIds
    ) throws Exception {
        stationService.loadByIds(locationId,stationIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> stationIds){
        stationService.deleteByIds(stationIds);
        return ResponseEntity.ok().build();
    }
}
