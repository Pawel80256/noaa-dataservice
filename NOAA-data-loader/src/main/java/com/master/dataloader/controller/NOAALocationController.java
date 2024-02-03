package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAALocation;
import com.master.dataloader.models.NOAALocationCategory;
import com.master.dataloader.repository.NOAALocationRepository;
import com.master.dataloader.service.NOAALocationService;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("NOAA/location")
public class NOAALocationController {

    private final NOAALocationService noaaLocationService;

    public NOAALocationController(NOAALocationService noaaLocationService) {
        this.noaaLocationService = noaaLocationService;
    }

    @GetMapping
    public ResponseEntity<PaginationWrapper<NOAALocation>> getAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
            @RequestParam(name = "datasetId", required = false) String datasetId,
            @RequestParam(name = "dataCategoryId", required = false) String dataCategoryId,
            @RequestParam(name = "locationCategoryId", required = false) String locationCategoryId,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
            ) throws Exception {

        return ResponseEntity.ok(
                noaaLocationService.getAll(limit,offset,datasetId,dataCategoryId,locationCategoryId,startDate,endDate)
        );
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
            @RequestParam(name = "datasetId", required = false) String datasetId,
            @RequestParam(name = "dataCategoryId", required = false) String dataCategoryId,
            @RequestParam(name = "locationCategoryId", required = false) String locationCategoryId,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) throws Exception {
        noaaLocationService.loadAll(limit,offset,datasetId,dataCategoryId,locationCategoryId,startDate,endDate);
        return ResponseEntity.ok().build();
    }
}
