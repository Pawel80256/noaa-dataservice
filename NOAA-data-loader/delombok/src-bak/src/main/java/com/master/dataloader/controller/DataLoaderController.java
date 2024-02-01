package com.master.dataloader.controller;

import com.master.dataloader.service.file_reading.DataLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data-loader")
public class DataLoaderController {
    private final DataLoaderService dataLoaderService;
    @PutMapping("/load-metadata")
    public ResponseEntity<Void> loadMetadata(){
        dataLoaderService.loadMetadata();
        return ResponseEntity.ok().build();
    }
    //minuta
    @PutMapping("/load-station-data")
    public ResponseEntity<Void> loadStationData(){
        dataLoaderService.loadStationData();
        return ResponseEntity.ok().build();
    }

    //30 sekund
    @PutMapping("/load-inventory-data")
    public ResponseEntity<Void> loadInventoryData(){
        dataLoaderService.loadInventoryData();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/load-measurements")
    public ResponseEntity<Void> loadMeasurements(@RequestParam String fileName){
        dataLoaderService.loadMeasurements(fileName);
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("/clear-database")
//    public ResponseEntity<Void> clearDatabase(){
//        dataLoaderService.clearDatabase();
//        return ResponseEntity.ok().build();
//    }

}
