package com.master.dataloader.controller;

import com.master.dataloader.dtos.NOAALocationDto;
import com.master.dataloader.service.NOAALocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("NOAA/location")
public class NOAALocationController {

    private final NOAALocationService noaaLocationService;

    public NOAALocationController(NOAALocationService noaaLocationService) {
        this.noaaLocationService = noaaLocationService;
    }

    @GetMapping
    public ResponseEntity<List<NOAALocationDto>> getAll(
            @RequestParam(name = "locationCategoryId") String locationCategoryId
    ){
        return ResponseEntity.ok(noaaLocationService.getAll(locationCategoryId));
    }

    @GetMapping("/remote")
    public ResponseEntity<List<NOAALocationDto>> getAllRemote(
            @RequestParam(name = "locationCategoryId") String locationCategoryId
    ) throws Exception {
        return ResponseEntity.ok(noaaLocationService.getAllRemoteDtos(locationCategoryId));
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadByIds(
            @RequestParam(name = "locationCategoryId") String locationCategoryId,
            @RequestBody List<String> locationIds
    ) throws Exception {
        noaaLocationService.loadByIds(locationCategoryId,locationIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteByIds")
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> locationsIds){
        noaaLocationService.deleteLocationsByIds(locationsIds);
        return ResponseEntity.ok().build();
    }
}
