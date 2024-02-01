package com.master.dataloader.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class NOAAData {
    @Id
    private UUID id;

    @ManyToOne
    private NOAADataType dataType;

    @ManyToOne
    private NOAAStation station;

    private String attributes;
}
