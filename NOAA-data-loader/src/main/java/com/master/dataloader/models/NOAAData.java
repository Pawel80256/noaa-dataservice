package com.master.dataloader.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NOAAData {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "data_type")
    @JsonProperty("datatype")
    private NOAADataType dataType;

    @ManyToOne
    @JoinColumn(name = "station")
    private NOAAStation station;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "attributes")
    private String attributes;


    public UUID getId() {
        return id;
    }

    public NOAADataType getDataType() {
        return dataType;
    }

    public NOAAStation getStation() {
        return station;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDataType(NOAADataType dataType) {
        this.dataType = dataType;
    }

    public void setStation(NOAAStation station) {
        this.station = station;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
