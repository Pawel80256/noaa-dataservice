package com.master.dataloader.controller;

import com.master.dataloader.dtos.MeasurementDto;
import com.master.dataloader.service.MeasurementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("NOAA/data")
public class MeasurementController {


    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }


    @GetMapping("/remote")
    public ResponseEntity<List<MeasurementDto>> getAllRemote(
            @RequestParam(name = "datasetId", required = true) String datasetId,
            @RequestParam(name = "dataTypeId", required = false) String dataTypeId,
            @RequestParam(name = "locationId", required = false) String locationId,
            @RequestParam(name = "stationId", required = false) String stationId,
            @RequestParam(name = "startDate", required = true) LocalDate startDate,
            @RequestParam(name = "endDate", required = true) LocalDate endDate
    ) throws Exception {
        return ResponseEntity.ok(
                measurementService.getAllDtos(datasetId,dataTypeId,locationId,stationId,startDate,endDate)
        );
    }

    @PutMapping("/load")
    public ResponseEntity<Void> load(
            @RequestParam(name = "datasetId", required = true) String datasetId,
            @RequestParam(name = "dataTypeId", required = false) String dataTypeId,
            @RequestParam(name = "stationId", required = false) String stationId,
            @RequestParam(name = "startDate", required = true) LocalDate startDate,
            @RequestParam(name = "endDate", required = true) LocalDate endDate
    ) throws Exception {
        measurementService.load(datasetId,dataTypeId,stationId,startDate,endDate);
        return ResponseEntity.ok().build();
    }
}
