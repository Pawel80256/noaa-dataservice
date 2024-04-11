package com.example.noaadatacalculationmodule.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MeasurementExtremeValuesDto {
    private Map<String, Integer> minValue;
    private Map<String, Integer> maxValue;
}
