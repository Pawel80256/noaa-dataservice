package com.example.noaadatacalculationmodule.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class Station {
    private String id;

    private Double elevation;

    @JsonProperty("mindate")
    private LocalDate minDate;

    @JsonProperty("maxdate")
    private LocalDate maxDate;

    private Double latitude;

    private String name;

    private Double dataCoverage;

    private String elevationUnit;

    private Double longitude;

    @JsonIgnore
    private Location location;

    private List<DataType> dataTypes;

    private String source;
}
