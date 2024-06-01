package com.example.noaadatacalculationmodule.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Measurement {
    private String id;

    private DataType datatype;

    private Station station;

    private LocalDate date;

    private String attributes;

    private Integer value;

    private String source;

    private String comment;
}
