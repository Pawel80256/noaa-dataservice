package com.master.dataloader.controller;

import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataset;
import com.master.dataloader.service.NOAADatasetService;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<List<NOAADataset>> getAll(){
        return ResponseEntity.ok(noaaDatasetService.getAll());
    }

    @GetMapping("/remote")
    public ResponseEntity<PaginationWrapper<NOAADataset>> getAllRemote(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ) throws Exception {
        return ResponseEntity.ok(noaaDatasetService.getAllRemote(limit,offset));
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadAllDatasets() throws Exception {
        noaaDatasetService.loadAll();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/loadByIds")
    public ResponseEntity<Void> loadDatasetsByIds(@RequestBody List<String> ids) throws Exception {
        noaaDatasetService.loadByIds(ids);
        return ResponseEntity.ok().build();
    }

}
