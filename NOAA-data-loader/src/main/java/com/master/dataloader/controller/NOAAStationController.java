package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAALocation;
import com.master.dataloader.models.NOAAStation;
import com.master.dataloader.repository.NOAAStationRepository;
import com.master.dataloader.service.NOAAStationService;
import com.master.dataloader.utils.Utils;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("NOAA/station")
public class NOAAStationController {

    private final NOAAStationRepository noaaStationRepository;
    private final NOAAStationService noaaStationService;

    public NOAAStationController(NOAAStationRepository noaaStationRepository, NOAAStationService noaaStationService) {
        this.noaaStationRepository = noaaStationRepository;
        this.noaaStationService = noaaStationService;
    }

    @GetMapping
    public ResponseEntity<PaginationWrapper<NOAAStation>> getAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
            @RequestParam(name = "locationId", required = false) String locationId,
            @RequestParam(name = "dataCategoryId", required = false) String dataCategoryId,
            @RequestParam(name = "dataTypeId", required = false) String dataTypeId,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) throws Exception {
        return ResponseEntity.ok(
                noaaStationService.getAll(limit,offset,locationId,dataCategoryId,dataTypeId,startDate,endDate)
        );
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<NOAAStation> getById(@PathVariable("stationId") String stationId) throws Exception {
        return ResponseEntity.ok(noaaStationService.getById(stationId));
    }

//    @PutMapping("/load")
//    public ResponseEntity<Void> loadAll(
//            @RequestParam(name = "limit") Integer limit,
//            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
//            @RequestParam(name = "locationId", required = false) String locationId,
//            @RequestParam(name = "dataCategoryId", required = false) String dataCategoryId,
//            @RequestParam(name = "dataTypeId", required = false) String dataTypeId,
//            @RequestParam(name = "startDate", required = false) LocalDate startDate,
//            @RequestParam(name = "endDate", required = false) LocalDate endDate
//    ) throws Exception {
//        noaaStationService.loadAll(limit,offset,locationId,dataCategoryId,dataTypeId,startDate,endDate);
//        return ResponseEntity.ok().build();
//    }
//
    @GetMapping("/location/{locationId}")
    public ResponseEntity<NOAAStation> getByLocationId (@PathVariable("locationId") String locationId) throws Exception {
        return ResponseEntity.ok(noaaStationService.getByLocationId(locationId));
    }

    @PutMapping("/load/{locationId}")
    public ResponseEntity<Void> loadByLocationId(@PathVariable("locationId") String locationId) throws Exception {
        noaaStationService.loadByLocationId(locationId);
        return ResponseEntity.ok().build();
    }


}
