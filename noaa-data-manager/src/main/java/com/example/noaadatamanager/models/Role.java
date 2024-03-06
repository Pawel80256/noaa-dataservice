package com.example.noaadatamanager.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "id")
    private String id;

    public Role() {
    }

    public Role(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
