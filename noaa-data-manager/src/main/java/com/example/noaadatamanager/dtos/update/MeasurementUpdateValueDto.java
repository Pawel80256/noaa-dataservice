package com.example.noaadatamanager.dtos.update;

public class MeasurementUpdateValueDto implements UpdateDto{
    private String measurementId;
    private Integer newValue;

    @Override
    public String getEntityId() {
        return measurementId;
    }

    @Override
    public Integer getUpdatedFieldValue() {
        return newValue;
    }

//    public void setMeasurementId(String measurementId) {
//        this.measurementId = measurementId;
//    }
//
//    public void setNewValue(Integer newValue) {
//        this.newValue = newValue;
//    }
}
