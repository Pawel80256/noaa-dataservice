package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.dtos.NOAAStationDto;
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

    private final NOAAStationService noaaStationService;

    public NOAAStationController(NOAAStationService noaaStationService) {
        this.noaaStationService = noaaStationService;
    }

    @GetMapping
    public ResponseEntity<List<NOAAStationDto>> getAll(){
        return ResponseEntity.ok(noaaStationService.getAll());
    }

    @GetMapping ("/remote")
    ResponseEntity<List<NOAAStationDto>> getAllRemote(@RequestParam(name = "locationId") String locationId) throws Exception {
        return ResponseEntity.ok(noaaStationService.getAllRemoteDtos(locationId));
    }

    @PutMapping("/load")
    ResponseEntity<Void> loadByIds(
            @RequestParam(name = "locationId") String locationId,
            @RequestBody List<String> stationIds
    ) throws Exception {
        noaaStationService.loadByIds(locationId,stationIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> stationIds){
        noaaStationService.deleteByIds(stationIds);
        return ResponseEntity.ok().build();
    }
}
