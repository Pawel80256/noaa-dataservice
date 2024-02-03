package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataset;
import com.master.dataloader.repository.NOAADatasetRepository;
import com.master.dataloader.service.NOAADatasetService;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("NOAA/datasets")
public class NOAADatasetController {

    private final NOAADatasetService noaaDatasetService;

    public NOAADatasetController(NOAADatasetService noaaDatasetService) {
        this.noaaDatasetService = noaaDatasetService;
    }

    @GetMapping()
    public ResponseEntity<PaginationWrapper<NOAADataset>> getAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ) throws Exception {
        return ResponseEntity.ok(noaaDatasetService.getAll(limit,offset));
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadAllDatasets(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ) throws Exception {
        noaaDatasetService.loadAll(limit,offset);
        return ResponseEntity.ok().build();
    }

}
