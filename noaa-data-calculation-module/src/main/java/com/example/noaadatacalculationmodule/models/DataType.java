package com.example.noaadatacalculationmodule.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DataType {
    private String id;

    private LocalDate minDate;

    private LocalDate maxDate;

    private String name;

    private Double dataCoverage;
}
