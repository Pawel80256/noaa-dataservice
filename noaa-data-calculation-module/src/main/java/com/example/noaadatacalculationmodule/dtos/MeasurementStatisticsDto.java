package com.example.noaadatacalculationmodule.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeasurementStatisticsDto {
    private Double average;
    private Double standardDeviation;
    private Double median;
    private MeasurementExtremeValuesDto measurementExtremeValuesDto;
}
