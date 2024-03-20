package com.example.noaadatamanager.dtos.update;

public class StationUpdateNameDto implements UpdateDto{

    private String stationId;

    private String newName;

    @Override
    public String getEntityId() {
        return stationId;
    }

    @Override
    public String getUpdatedFieldValue() {
        return newName;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

}
