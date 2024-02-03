package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.repository.NOAADataTypeRepository;
import com.master.dataloader.service.NOAADataTypeService;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/NOAA/datatypes")
public class NOAADataTypeController {

    private final NOAADataTypeRepository noaaDataTypeRepository;
    private final NOAADataTypeService noaaDataTypeService;

    public NOAADataTypeController(NOAADataTypeRepository noaaDataTypeRepository, NOAADataTypeService noaaDataTypeService) {
        this.noaaDataTypeRepository = noaaDataTypeRepository;
        this.noaaDataTypeService = noaaDataTypeService;
    }

    @GetMapping
    public ResponseEntity<PaginationWrapper<NOAADataType>> getAll(
            @RequestParam(name = "datasetId", required = false) String datasetId,
            @RequestParam(name = "locationId", required = false) String locationId,
            @RequestParam(name = "stationId", required = false) String stationId,
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ) throws Exception {
            return ResponseEntity.ok(noaaDataTypeService.getAll(limit,offset,datasetId,locationId,stationId));
    }

    @GetMapping("/{dataTypeId}")
    public ResponseEntity<NOAADataType> getById(@PathVariable("dataTypeId") String dataTypeId) throws Exception {
        return ResponseEntity.ok(noaaDataTypeService.getById(dataTypeId));
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadDataTypes(
            @RequestParam(name = "datasetId", required = false) String datasetId,
            @RequestParam(name = "locationId", required = false) String locationId,
            @RequestParam(name = "stationId", required = false) String stationId,
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ) throws Exception {
        noaaDataTypeService.loadAll(limit,offset,datasetId,locationId,stationId);
        return ResponseEntity.ok().build();
    }
}
