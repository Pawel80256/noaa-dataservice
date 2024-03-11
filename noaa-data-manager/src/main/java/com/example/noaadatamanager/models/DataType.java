package com.example.noaadatamanager.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "data_type")
public class DataType {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "min_date")
    @JsonProperty("mindate")
    private LocalDate minDate;

    @Column(name = "max_date")
    @JsonProperty("maxdate")
    private LocalDate maxDate;

    @Column(name = "name")
    private String name;

    @Column(name = "data_coverage")
    @JsonProperty("datacoverage")
    private Double dataCoverage;

    public DataType() {
    }

    public DataType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public String getName() {
        return name;
    }

    public Double getDataCoverage() {
        return dataCoverage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDataCoverage(Double dataCoverage) {
        this.dataCoverage = dataCoverage;
    }
}
