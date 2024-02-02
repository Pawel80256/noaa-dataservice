package com.master.dataloader.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "station")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NOAAStation {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "elevation")
    private Double elevation;

    @Column(name = "min_date")
    @JsonProperty("mindate")
    private LocalDate minDate;

    @Column(name = "max_date")
    @JsonProperty("maxdate")
    private LocalDate maxDate;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "name")
    private String name;

    @Column(name = "data_coverage")
    @JsonProperty("datacoverage")
    private Double dataCoverage;

    @Column(name = "elevation_unit")
    @JsonProperty("elevationUnit")
    private String elevationUnit;

    @Column(name = "longitude")
    private Double longitude;

    public String getId() {
        return id;
    }

    public Double getElevation() {
        return elevation;
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    public Double getDataCoverage() {
        return dataCoverage;
    }

    public String getElevationUnit() {
        return elevationUnit;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDataCoverage(Double dataCoverage) {
        this.dataCoverage = dataCoverage;
    }

    public void setElevationUnit(String elevationUnit) {
        this.elevationUnit = elevationUnit;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
