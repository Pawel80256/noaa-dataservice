package com.example.noaadatamanager.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NOAAData {

    @Id
    @Column(name = "id")
    private String id;

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

    @Column(name = "value")
    private Integer value;

    @Column(name = "source")
    private String source;

    public String getId() {
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

    public void setId(String id) {
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

    public static class Builder {
        private NOAAData noaaData;

        public Builder() {
            noaaData = new NOAAData();
        }

        public Builder id(String id) {
            noaaData.id = id;
            return this;
        }

        public Builder dataType(NOAADataType dataType) {
            noaaData.dataType = dataType;
            return this;
        }

        public Builder station(NOAAStation station) {
            noaaData.station = station;
            return this;
        }

        public Builder date(LocalDate date) {
            noaaData.date = date;
            return this;
        }

        public Builder attributes(String attributes) {
            noaaData.attributes = attributes;
            return this;
        }

        public Builder value(Integer value) {
            noaaData.value = value;
            return this;
        }

        public Builder source(String source) {
            noaaData.source = source;
            return this;
        }

        public NOAAData build() {
            return noaaData;
        }
    }
}
