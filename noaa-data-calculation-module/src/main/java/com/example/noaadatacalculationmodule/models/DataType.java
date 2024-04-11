package com.example.noaadatacalculationmodule.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class DataType {
    private String id;

    private LocalDate minDate;

    private LocalDate maxDate;

    private String name;

    private Double dataCoverage;
}
