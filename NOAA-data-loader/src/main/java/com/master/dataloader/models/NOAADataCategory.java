package com.master.dataloader.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class NOAADataCategory {
    @Id
    private String id;
    private String name;
}
