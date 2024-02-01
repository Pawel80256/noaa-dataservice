package com.master.dataloader.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class NOAALocation {
    @Id
    private String id;
    private LocalDate minDate;
    private LocalDate maxDate;
    private Double dataCoverage;
    private String name;
}
