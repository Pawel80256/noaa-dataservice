package com.example.noaadatacalculationmodule.dtos;

import lombok.Getter;
import lombok.Setter;


public class MeasurementStatisticsDto {
    private Double average;
    private Double standardDeviation;
    private Double median;
    private MeasurementExtremeValuesDto measurementExtremeValuesDto;

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public Double getMedian() {
        return median;
    }

    public void setMedian(Double median) {
        this.median = median;
    }

    public MeasurementExtremeValuesDto getMeasurementExtremeValuesDto() {
        return measurementExtremeValuesDto;
    }

    public void setMeasurementExtremeValuesDto(MeasurementExtremeValuesDto measurementExtremeValuesDto) {
        this.measurementExtremeValuesDto = measurementExtremeValuesDto;
    }
}
