package com.master.dataloader.dtos;

import com.master.dataloader.models.Location;

import java.time.LocalDate;

public class LocationDto {
    private String id;
    private LocalDate minDate;
    private LocalDate maxDate;
    private Double dataCoverage;
    private String name;
    private String locationCategoryId;
    private String parentId;
    private Boolean isLoaded;

    public LocationDto(Location entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.minDate = entity.getMinDate();
        this.maxDate = entity.getMaxDate();
        this.dataCoverage = entity.getDataCoverage();
        this.locationCategoryId = entity.getNoaaLocationCategory().getId();
        this.parentId = entity.getParent() != null ? entity.getParent().getId() : null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

    public Double getDataCoverage() {
        return dataCoverage;
    }

    public void setDataCoverage(Double dataCoverage) {
        this.dataCoverage = dataCoverage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationCategoryId() {
        return locationCategoryId;
    }

    public void setLocationCategoryId(String locationCategoryId) {
        this.locationCategoryId = locationCategoryId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Boolean getLoaded() {
        return isLoaded;
    }

    public void setLoaded(Boolean loaded) {
        isLoaded = loaded;
    }
}
