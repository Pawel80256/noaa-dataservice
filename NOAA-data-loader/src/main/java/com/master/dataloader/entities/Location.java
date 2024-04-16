package com.master.dataloader.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "location")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "min_date")
    @JsonProperty("mindate")
    private LocalDate minDate;

    @Column(name = "max_date")
    @JsonProperty("maxdate")
    private LocalDate maxDate;

    @Column(name = "data_coverage")
    @JsonProperty("datacoverage")
    private Double dataCoverage;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "location_category")
    @JsonIgnore
    private LocationCategory locationCategory;

    @ManyToOne
    @JoinColumn(name = "parent")
    @JsonIgnore
    private Location parent;

    public Location() {
    }

    public Location(String id) {
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

    public Double getDataCoverage() {
        return dataCoverage;
    }

    public String getName() {
        return name;
    }

    public LocationCategory getNoaaLocationCategory() {
        return locationCategory;
    }

    public Location getParent() {
        return parent;
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

    public void setDataCoverage(Double dataCoverage) {
        this.dataCoverage = dataCoverage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNoaaLocationCategory(LocationCategory locationCategory) {
        this.locationCategory = locationCategory;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }
}
