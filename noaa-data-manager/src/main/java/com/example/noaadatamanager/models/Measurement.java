package com.example.noaadatamanager.models;

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
    private Station station;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "attributes")
    private String attributes;

    @Column(name = "value")
    private Integer value;

    @Column(name = "source")
    private String source;

    @Column(name = "comment")
    private String comment;

    public String getId() {
        return id;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Station getStation() {
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

    public void setStation(Station station) {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static class Builder {
        private Measurement measurement;

        public Builder() {
            measurement = new Measurement();
        }

        public Builder id(String id) {
            measurement.id = id;
            return this;
        }

        public Builder dataType(DataType dataType) {
            measurement.dataType = dataType;
            return this;
        }

        public Builder station(Station station) {
            measurement.station = station;
            return this;
        }

        public Builder date(LocalDate date) {
            measurement.date = date;
            return this;
        }

        public Builder attributes(String attributes) {
            measurement.attributes = attributes;
            return this;
        }

        public Builder value(Integer value) {
            measurement.value = value;
            return this;
        }

        public Builder source(String source) {
            measurement.source = source;
            return this;
        }

        public Measurement build() {
            return measurement;
        }
    }
}
