package com.master.dataloader.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Measurement {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "data_type")
    @JsonProperty("datatype")
    private DataType dataType;

    @ManyToOne
    @JoinColumn(name = "station")
    private NOAAStation station;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "attributes")
    private String attributes;

    @Column(name = "value")
    private Integer value;

    @Column(name = "source")
    private String source;


    public String getId() {
        return id;
    }

    public DataType getDataType() {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setStation(NOAAStation station) {
        this.station = station;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
