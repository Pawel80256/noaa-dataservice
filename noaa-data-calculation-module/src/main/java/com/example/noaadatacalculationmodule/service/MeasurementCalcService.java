package com.example.noaadatacalculationmodule.service;

import com.example.noaadatacalculationmodule.dtos.MeasurementExtremeValuesDto;
import com.example.noaadatacalculationmodule.dtos.MeasurementStatisticsDto;
import com.example.noaadatacalculationmodule.exceptions.ValidationException;
import com.example.noaadatacalculationmodule.models.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MeasurementCalcService {

    private final Logger log = LoggerFactory.getLogger(MeasurementCalcService.class);

    //walidacja tylko w metodach liczacych pojedyncze parametry, na potrzeby testów wydajnosciowych
    public MeasurementStatisticsDto calculateStatistics(List<Measurement> measurements){
        var measurementStatisticsDto = new MeasurementStatisticsDto();
        measurementStatisticsDto.setAverage(calculateAverageValue(measurements));
        measurementStatisticsDto.setMedian(calculateMedian(measurements));
        measurementStatisticsDto.setStandardDeviation(calculateStandardDeviation(measurements));
        measurementStatisticsDto.setMeasurementExtremeValuesDto(calculateExtremeValues(measurements));
        return measurementStatisticsDto;
    }

    public Double calculateAverageValue(List<Measurement> measurements){
        log.info("Starting average calculation for measurements [" +
                String.join(",",measurements.stream().map(Measurement::getId).toList())
                + "]"
        );

        if(measurements.size() < 2){
            String message = "At least 2 measurements required";
            log.error(message);
            throw new ValidationException(message);
        }

        if(measurements.size() > 10){
            String message = "Max 10 measurements allowed";
            log.error(message);
            throw new ValidationException(message);
        }

        int counter = 0;
        double valueSum = 0;

        for(Measurement measurement : measurements){
            counter ++;
            valueSum += measurement.getValue();
        }

        log.info("Average calculation finished");
        return (valueSum/counter);
    }

    public MeasurementExtremeValuesDto calculateExtremeValues(List<Measurement> measurements) {
        if (measurements.isEmpty()) {
            return new MeasurementExtremeValuesDto();
        }

        Map<String, Integer> minValueMap = new HashMap<>();
        Map<String, Integer> maxValueMap = new HashMap<>();
        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;

        for (Measurement measurement : measurements) {
            int currentValue = measurement.getValue();
            if (currentValue < minValue) {
                minValue = currentValue;
                minValueMap.clear();
                minValueMap.put(measurement.getId(), currentValue);
            } else if (currentValue == minValue) {
                minValueMap.put(measurement.getId(), currentValue);
            }

            if (currentValue > maxValue) {
                maxValue = currentValue;
                maxValueMap.clear();
                maxValueMap.put(measurement.getId(), currentValue);
            } else if (currentValue == maxValue) {
                maxValueMap.put(measurement.getId(), currentValue);
            }
        }

        MeasurementExtremeValuesDto result = new MeasurementExtremeValuesDto();
        result.setMinValue(minValueMap);
        result.setMaxValue(maxValueMap);
        return result;
    }

    public Double calculateStandardDeviation(List<Measurement> measurements) {
        double mean = calculateAverageValue(measurements);
        double variance = 0.0;

        for (Measurement measurement : measurements) {
            variance += Math.pow(measurement.getValue() - mean, 2);
        }

        variance = variance / measurements.size();
        return Math.sqrt(variance);
    }

    public Double calculateMedian(List<Measurement> measurements) {
        List<Integer> values = measurements.stream()
                .map(Measurement::getValue)
                .sorted()
                .collect(Collectors.toList());

        int size = values.size();
        if (size == 0) {
            return null;
        }

        if (size % 2 == 0) {
            return (values.get(size / 2 - 1) + values.get(size / 2)) / 2.0;
        } else {
            return (double) values.get(size / 2);
        }
    }
}
