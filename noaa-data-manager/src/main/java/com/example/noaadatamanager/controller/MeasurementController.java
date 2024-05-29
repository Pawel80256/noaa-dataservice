package com.example.noaadatamanager.controller;

import com.example.noaadatamanager.annotations.RequestAuthorization;
import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.dtos.output.MeasurementExtremeValuesDto;
import com.example.noaadatamanager.dtos.output.MeasurementStatisticsDto;
import com.example.noaadatamanager.dtos.update.MeasurementUpdateCommentDto;
import com.example.noaadatamanager.dtos.update.MeasurementUpdateValueDto;
import com.example.noaadatamanager.service.MeasurementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("measurements")
public class MeasurementController {
    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping
    @RequestAuthorization(roles = {"ADMIN","MEASUREMENT_MANAGER"})
    public ResponseEntity<String> create(@RequestBody MeasurementInputDto measurementInputDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(measurementService.create(measurementInputDto));
    }

    @DeleteMapping
    @RequestAuthorization(roles = {"ADMIN","MEASUREMENT_MANAGER"})
    public ResponseEntity<Void> delete(@RequestParam(name = "measurementId") String measurementId){
        measurementService.delete(measurementId);
        return ResponseEntity.ok().build();
//        return ResponseEntity.ok(measurementService.delete(measurementId));
    }

    @PutMapping("/value")
    @RequestAuthorization(roles = {"ADMIN","MEASUREMENT_MANAGER"})
    public ResponseEntity<String> updateValue(@RequestBody MeasurementUpdateValueDto dto){
        return ResponseEntity.ok(measurementService.updateValue(dto));
    }

    @PutMapping("/comment")
    @RequestAuthorization(roles = {"ADMIN","MEASUREMENT_MANAGER"})
    public ResponseEntity<String> updateComment(@RequestBody MeasurementUpdateCommentDto dto){
        return ResponseEntity.ok(measurementService.updateComment(dto));
    }

    @PostMapping("/statistics")
    public ResponseEntity<MeasurementStatisticsDto> calculateStatistics(@RequestBody List<String> mesruementIds){
        return ResponseEntity.ok(measurementService.calculateStatistics(mesruementIds));
    }

    @PostMapping("/average")
    public ResponseEntity<Double> calculateAverage(@RequestBody List<String> measurementIds){
        return ResponseEntity.ok(measurementService.calculateAverage(measurementIds));
    }

    @PostMapping("/extremes")
    public ResponseEntity<MeasurementExtremeValuesDto> calculateExtremeValues(@RequestBody List<String> measurementIds){
        return ResponseEntity.ok(measurementService.calculateExtremeValues(measurementIds));
    }

    @PostMapping("/standard-deviation")
    public ResponseEntity<Double> calculateStandardDeviation(@RequestBody List<String> measurementIds){
        return ResponseEntity.ok(measurementService.calculateStandardDeviation(measurementIds));
    }

    @PostMapping("/median")
    public ResponseEntity<Double> calculateMedian(@RequestBody List<String> measurementIds){
        return ResponseEntity.ok(measurementService.calculateMedian(measurementIds));
    }
}
