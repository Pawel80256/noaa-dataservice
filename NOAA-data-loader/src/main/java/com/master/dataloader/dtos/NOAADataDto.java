package com.master.dataloader.dtos;

import com.master.dataloader.models.NOAAData;

import java.time.LocalDate;
import java.util.UUID;

public class NOAADataDto {
    private String id;
    private String dataTypeId;
    private String stationId;
    private LocalDate date;
    private String attributes;
    private Integer value;
    private boolean isLoaded;

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public NOAADataDto(NOAAData entity){
        this.id = entity.getId();
        this.dataTypeId = entity.getDataType().getId();
        this.stationId = entity.getStation().getId();
        this.date = entity.getDate();
        this.attributes = entity.getAttributes();
        this.value = entity.getValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(String dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}
