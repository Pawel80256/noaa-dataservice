package com.example.noaadatacalculationmodule.controller;

import com.example.noaadatacalculationmodule.dtos.MeasurementExtremeValuesDto;
import com.example.noaadatacalculationmodule.dtos.MeasurementStatisticsDto;
import com.example.noaadatacalculationmodule.models.Measurement;
import com.example.noaadatacalculationmodule.service.MeasurementCalcService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("measurements")
@RequiredArgsConstructor
public class MeasurementCalcController {

    private final MeasurementCalcService measurementCalcService;

    @PostMapping("/statistics")
    public ResponseEntity<MeasurementStatisticsDto> calculateStatistics(@RequestBody List<Measurement> measurements){
        return ResponseEntity.ok(measurementCalcService.calculateStatistics(measurements));
    }

    @PostMapping("/average")
    public ResponseEntity<Double> calculateAverageValue(@RequestBody List<Measurement> measurements){
        return ResponseEntity.ok(measurementCalcService.calculateAverageValue(measurements));
    }

    @PostMapping("/extremes")
    public ResponseEntity<MeasurementExtremeValuesDto> getExtremeValues (@RequestBody List<Measurement> measurements){
        return ResponseEntity.ok(measurementCalcService.calculateExtremeValues(measurements));
    }

    @PostMapping("/standard-deviation")
    public ResponseEntity<Double> calculateStandardDeviation(@RequestBody List<Measurement> measurements){
        return ResponseEntity.ok(measurementCalcService.calculateStandardDeviation(measurements));
    }

    @PostMapping("/median")
    public ResponseEntity<Double> calculateMedian(@RequestBody List<Measurement> measurements){
        return ResponseEntity.ok(measurementCalcService.calculateMedian(measurements));
    }
}
