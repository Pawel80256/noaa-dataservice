package com.master.dataloader.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "dataset")
public class Dataset {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "data_coverage")
    @JsonProperty("datacoverage")
    private Double dataCoverage;

    @Column(name = "min_date")
    @JsonProperty("mindate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate minDate;

    @Column(name = "max_date")
    @JsonProperty("maxdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate maxDate;

    @Column(name = "name")
    private String name;

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public Double getDataCoverage() {
        return dataCoverage;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDataCoverage(Double dataCoverage) {
        this.dataCoverage = dataCoverage;
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
}
