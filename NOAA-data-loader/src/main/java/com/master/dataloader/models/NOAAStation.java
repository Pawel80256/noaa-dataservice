package com.master.dataloader.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

//todo: add dataCategories and dataTypes fields, can be fetched by stationId
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

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonIgnore
    private Location location;

    @ManyToMany
    @JoinTable(
        name = "stations_data_types",
            joinColumns = @JoinColumn(name = "station_id"),
            inverseJoinColumns = @JoinColumn(name = "data_type_id")
    )
    @JsonIgnore
    private List<DataType> dataTypes;

    @Column(name = "source")
    private String source;

    public NOAAStation(String id) {
        this.id = id;
    }

    public NOAAStation() {

    }

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

    public List<DataType> getDataTypes() {
        return dataTypes;
    }

    public Location getNoaaLocation() {
        return location;
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

    public void setDataTypes(List<DataType> dataTypes) {
        this.dataTypes = dataTypes;
    }

    public void setNoaaLocation(Location location) {
        this.location = location;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
