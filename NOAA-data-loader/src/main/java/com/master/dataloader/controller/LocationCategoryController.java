package com.master.dataloader.controller;

import com.master.dataloader.dtos.LocationCategoryDto;
import com.master.dataloader.service.LocationCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("NOAA/locationcategory")
public class LocationCategoryController {

    private final LocationCategoryService locationCategoryService;

    public LocationCategoryController(LocationCategoryService locationCategoryService) {
        this.locationCategoryService = locationCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<LocationCategoryDto>> getAll(){
        return ResponseEntity.ok(locationCategoryService.getAll());
    }

    @GetMapping("/remote")
    public ResponseEntity<List<LocationCategoryDto>> getAllRemote() throws Exception {
        return ResponseEntity.ok(
                locationCategoryService.getAllRemoteDtos()
        );
    }

    @PutMapping("/loadByIds")
    public ResponseEntity<Void> loadByIds(@RequestBody List<String> ids) throws Exception {
        locationCategoryService.loadByIds(ids);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> ids) {
        locationCategoryService.deleteByIds(ids);
        return ResponseEntity.ok().build();
    }

}
