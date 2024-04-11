package com.example.noaadatacalculationmodule.service;

import com.example.noaadatacalculationmodule.dtos.MeasurementExtremeValuesDto;
import com.example.noaadatacalculationmodule.models.Measurement;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeasurementCalcService {

    public Double calculateAverageValue(List<Measurement> measurements){
        int counter = 0;
        double valueSum = 0;

        for(Measurement measurement : measurements){
            counter ++;
            valueSum += measurement.getValue();
        }

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
}
