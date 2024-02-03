package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAAData;
import com.master.dataloader.models.NOAAStation;
import com.master.dataloader.repository.NOAADataRepository;
import com.master.dataloader.repository.NOAAStationRepository;
import com.master.dataloader.service.NOAADataService;
import com.master.dataloader.service.NOAAStationService;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("NOAA/data")
public class NOAADataController {


    private final NOAADataService noaaDataService;

    public NOAADataController( NOAADataService noaaDataService) {
        this.noaaDataService = noaaDataService;
    }

    @GetMapping
    public ResponseEntity<PaginationWrapper<NOAAData>> getAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
            @RequestParam(name = "datasetId", required = true) String datasetId,
            @RequestParam(name = "dataTypeId", required = false) String dataTypeId,
            @RequestParam(name = "locationId", required = false) String locationId,
            @RequestParam(name = "stationId", required = false) String stationId,
            @RequestParam(name = "startDate", required = true) LocalDate startDate,
            @RequestParam(name = "endDate", required = true) LocalDate endDate
    ) throws Exception {
        return ResponseEntity.ok(
                noaaDataService.getAll(limit,offset,datasetId,dataTypeId,locationId,stationId,startDate,endDate)
        );
    }

//    @PutMapping("/load")
//    public ResponseEntity<Void> loadAll(
//            @RequestParam(name = "limit") Integer limit,
//            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
//            @RequestParam(name = "datasetId", required = true, defaultValue = "GHCND") String datasetId,
//            @RequestParam(name = "dataTypeId", required = false) String dataTypeId,
//            @RequestParam(name = "locationId", required = false) String locationId,
//            @RequestParam(name = "stationId", required = false) String stationId,
//            @RequestParam(name = "startDate", required = true, defaultValue = "2012-10-03") LocalDate startDate,
//            @RequestParam(name = "endDate", required = true, defaultValue = "2012-11-10") LocalDate endDate
//    ){
//        try {
//            noaaDataService.loadAll(limit,offset,datasetId,dataTypeId,locationId,stationId,startDate,endDate);
//            return ResponseEntity.ok().build();
//        }catch (Exception e){
//            throw new RuntimeException(e);
//        }
//
//    }

    @PutMapping("/load")
    public ResponseEntity<Void> load(
            @RequestParam(name = "stationId", required = true) String stationId,
            @RequestParam(name = "startDate", required = true, defaultValue = "2012-10-03") LocalDate startDate,
            @RequestParam(name = "endDate", required = true, defaultValue = "2012-11-10") LocalDate endDate,
            @RequestParam(name = "datasetId", required = true, defaultValue = "GHCND") String datasetId,
            @RequestParam(name = "dataTypeId", required = false) String dataTypeId
    ) throws Exception {
        noaaDataService.loadByStationId(stationId,startDate,endDate,datasetId,dataTypeId);
        return ResponseEntity.ok().build();
    }
}
