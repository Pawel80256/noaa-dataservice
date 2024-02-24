package com.master.dataloader.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class NOAADataCategory {
    @Id
    private String id;
    private String name;

    public NOAADataCategory() {
    }

    public NOAADataCategory(String id, String name) {
        this.id = id;
        this.name = name;
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
}
