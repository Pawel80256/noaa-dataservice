package com.example.noaadatacalculationmodule.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Location {
    private String id;

    private LocalDate minDate;

    private LocalDate maxDate;

    private Double dataCoverage;

    private String name;

    private LocationCategory locationCategory;

    private Location parent;
}
