package com.master.dataloader.controller;

import com.master.dataloader.dtos.NOAADatasetDto;
import com.master.dataloader.service.NOAADatasetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("NOAA/datasets")
public class NOAADatasetController {

    private final NOAADatasetService noaaDatasetService;

    public NOAADatasetController(NOAADatasetService noaaDatasetService) {
        this.noaaDatasetService = noaaDatasetService;
    }

    @GetMapping()
    public ResponseEntity<List<NOAADatasetDto>> getAll(){
        return ResponseEntity.ok(noaaDatasetService.getAll());
    }

    @GetMapping("/remote")
    public ResponseEntity<List<NOAADatasetDto>> getAllRemote() throws Exception {
        return ResponseEntity.ok(noaaDatasetService.getAllRemoteDtos());
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadByIds(@RequestBody List<String> datasetsIds) throws Exception {
        noaaDatasetService.loadByIds(datasetsIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> ids){
        noaaDatasetService.deleteByIds(ids);
        return ResponseEntity.ok().build();
    }
}
