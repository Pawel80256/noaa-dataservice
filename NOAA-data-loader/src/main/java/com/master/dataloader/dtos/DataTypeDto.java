package com.master.dataloader.dtos;

import com.master.dataloader.models.DataType;

import java.time.LocalDate;

public class DataTypeDto {
    private String id;
    private LocalDate minDate;
    private LocalDate maxDate;
    private String name;
    private Double dataCoverage;
    private Boolean isLoaded;

    public DataTypeDto(DataType entity) {
        this.id = entity.getId();
        this.minDate = entity.getMinDate();
        this.maxDate = entity.getMaxDate();
        this.name = entity.getName();
        this.dataCoverage = entity.getDataCoverage();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDataCoverage() {
        return dataCoverage;
    }

    public void setDataCoverage(Double dataCoverage) {
        this.dataCoverage = dataCoverage;
    }

    public Boolean getLoaded() {
        return isLoaded;
    }

    public void setLoaded(Boolean isLoaded) {
        this.isLoaded = isLoaded;
    }
}
