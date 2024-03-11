package com.master.dataloader.controller;

import com.master.dataloader.dtos.DatasetDto;
import com.master.dataloader.service.DatasetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("NOAA/datasets")
public class DatasetController {

    private final DatasetService datasetService;

    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @GetMapping()
    public ResponseEntity<List<DatasetDto>> getAll(){
        return ResponseEntity.ok(datasetService.getAll());
    }

    @GetMapping("/remote")
    public ResponseEntity<List<DatasetDto>> getAllRemote() throws Exception {
        return ResponseEntity.ok(datasetService.getAllRemoteDtos());
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadByIds(@RequestBody List<String> datasetsIds) throws Exception {
        datasetService.loadByIds(datasetsIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> ids){
        datasetService.deleteByIds(ids);
        return ResponseEntity.ok().build();
    }
}
