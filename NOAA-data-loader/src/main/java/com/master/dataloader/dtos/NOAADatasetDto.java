package com.master.dataloader.dtos;

import com.master.dataloader.models.NOAADataset;

import java.time.LocalDate;

public class NOAADatasetDto {
    private String id;
    private String uid;
    private Double dataCoverage;
    private LocalDate minDate;
    private LocalDate maxDate;
    private String name;
    private Boolean isLoaded;

    public NOAADatasetDto(NOAADataset entity) {
        this.id = entity.getId();
        this.uid = entity.getUid();
        this.dataCoverage = entity.getDataCoverage();
        this.minDate = entity.getMinDate();
        this.maxDate = entity.getMaxDate();
        this.name = entity.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getDataCoverage() {
        return dataCoverage;
    }

    public void setDataCoverage(Double dataCoverage) {
        this.dataCoverage = dataCoverage;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLoaded() {
        return isLoaded;
    }

    public void setLoaded(Boolean loaded) {
        isLoaded = loaded;
    }
}
