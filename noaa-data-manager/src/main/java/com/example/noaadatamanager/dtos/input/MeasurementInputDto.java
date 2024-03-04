package com.example.noaadatamanager.dtos.input;

import java.time.LocalDate;

public class MeasurementInputDto {
    private String dataTypeId;
    private String stationId;
    private LocalDate date;
    private Integer value;

    public String getDataTypeId() {
        return dataTypeId;
    }

    public String getStationId() {
        return stationId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getValue() {
        return value;
    }


}
