package com.master.dataloader.controller;

import com.master.dataloader.dtos.LocationDto;
import com.master.dataloader.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("NOAA/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAll(
            @RequestParam(name = "locationCategoryId") String locationCategoryId
    ){
        return ResponseEntity.ok(locationService.getAll(locationCategoryId));
    }

    @GetMapping("/remote")
    public ResponseEntity<List<LocationDto>> getAllRemote(
            @RequestParam(name = "locationCategoryId") String locationCategoryId
    ) throws Exception {
        return ResponseEntity.ok(locationService.getAllRemoteDtos(locationCategoryId));
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadByIds(
            @RequestParam(name = "locationCategoryId") String locationCategoryId,
            @RequestBody List<String> locationIds
    ) throws Exception {
        locationService.loadByIds(locationCategoryId,locationIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteByIds")
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> locationsIds){
        locationService.deleteLocationsByIds(locationsIds);
        return ResponseEntity.ok().build();
    }
}
