package com.master.dataloader.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class NOAAStation {
    @Id
    private String id;
    private Double elevation;
    private LocalDate minDate;
    private LocalDate maxDate;
    private Double latitude;
    private String name;
    private Double dataCoverage;
    private String elevationUnit;
    private Double longitude;
}
