package com.example.noaadatamanager.dtos.input;

import java.time.LocalDate;

public class StationInputDto {
    private Double elevation;
    private LocalDate minDate;
    private LocalDate maxDate;
    private Double latitude;
    private Double longitude;
    private String name;
    private Double dataCoverage;
    private String elevationUnit;
    private String locationId;

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

    public Double getLongitude() {
        return longitude;
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

    public String getLocationId() {
        return locationId;
    }
}
