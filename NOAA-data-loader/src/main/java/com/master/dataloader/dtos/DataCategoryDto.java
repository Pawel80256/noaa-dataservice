package com.master.dataloader.dtos;

import com.master.dataloader.entities.DataCategory;

public class DataCategoryDto {
    private String id;
    private String name;
    private Boolean isLoaded;

    public DataCategoryDto(DataCategory entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
