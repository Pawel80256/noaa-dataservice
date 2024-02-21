package com.master.dataloader.dtos;

import com.master.dataloader.models.NOAAData;

import java.time.LocalDate;
import java.util.UUID;

public class NOAADataDto {
    private UUID id;
    private String dataTypeId;
    private String stationId;
    private LocalDate date;
    private String attributes;
    private Integer value;

    public NOAADataDto(NOAAData entity){
        this.id = entity.getId();
        this.dataTypeId = entity.getDataType().getId();
        this.stationId = entity.getStation().getId();
        this.date = entity.getDate();
        this.attributes = entity.getAttributes();
        this.value = entity.getValue();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
