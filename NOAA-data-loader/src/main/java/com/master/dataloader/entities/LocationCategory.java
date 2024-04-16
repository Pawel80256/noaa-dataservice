package com.master.dataloader.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "location_category")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationCategory {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    public LocationCategory() {
    }

    public LocationCategory(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
