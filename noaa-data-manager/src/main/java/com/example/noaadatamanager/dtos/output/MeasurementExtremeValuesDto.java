package com.example.noaadatamanager.dtos.output;

import java.util.Map;


public class MeasurementExtremeValuesDto {
    private Map<String, Integer> minValue;
    private Map<String, Integer> maxValue;

    public Map<String, Integer> getMinValue() {
        return minValue;
    }

    public void setMinValue(Map<String, Integer> minValue) {
        this.minValue = minValue;
    }

    public Map<String, Integer> getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Map<String, Integer> maxValue) {
        this.maxValue = maxValue;
    }
}

