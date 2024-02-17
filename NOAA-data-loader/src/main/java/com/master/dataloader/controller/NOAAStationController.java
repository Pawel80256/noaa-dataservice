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
    public ResponseEntity<List<NOAAStation>> getAll(){
        return ResponseEntity.ok(noaaStationService.getAll());
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<NOAAStation> getById(@PathVariable("stationId") String stationId) throws Exception {
        return ResponseEntity.ok(noaaStationService.getById(stationId));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<NOAAStation>> getByLocationId (@PathVariable("locationId") String locationId) throws Exception {
        return ResponseEntity.ok(noaaStationService.getRemoteByLocationId(locationId));
    }

    @PutMapping("/load/{locationId}")
    public ResponseEntity<Void> loadByIdsAndLocationId
            (@PathVariable("locationId") String locationId,
             @RequestBody List<String> stationIds) throws Exception {
        noaaStationService.loadByIdsAndLocationId(locationId,stationIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> stationIds){
        noaaStationService.deleteByIds(stationIds);
        return ResponseEntity.ok().build();
    }
}
