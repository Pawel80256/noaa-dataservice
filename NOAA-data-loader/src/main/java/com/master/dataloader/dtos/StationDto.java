package com.master.dataloader.dtos;

import com.master.dataloader.models.Station;

import java.time.LocalDate;

public class StationDto {
    private String id;
    private Double elevation;
    private LocalDate minDate;
    private LocalDate maxDate;
    private Double latitude;
    private String name;
    private Double dataCoverage;
    private String elevationUnit;
    private Double longitude;
    private String locationId;
    private Boolean isLoaded;

    public StationDto(Station entity) {
        this.id = entity.getId();
        this.elevation = entity.getElevation();
        this.minDate = entity.getMinDate();
        this.maxDate = entity.getMaxDate();
        this.latitude = entity.getLatitude();
        this.name = entity.getName();
        this.dataCoverage = entity.getDataCoverage();
        this.elevationUnit = entity.getElevationUnit();
        this.longitude = entity.getLongitude();
        this.locationId = entity.getNoaaLocation().getId();
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

    public String getLocationId() {
        return locationId;
    }

    public Boolean getLoaded() {
        return isLoaded;
    }

    public void setLoaded(Boolean loaded) {
        isLoaded = loaded;
    }
}
