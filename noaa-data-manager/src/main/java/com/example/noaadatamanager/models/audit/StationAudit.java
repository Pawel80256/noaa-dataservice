package com.example.noaadatamanager.models.audit;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(schema = "audit", name = "station_audit")
@Entity
public class StationAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "record_id")
    private String recordId;

    @Column(name = "operation")
    private String operation;

    @Column(name = "userr")
    private String user;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public static class Builder {
        private StationAudit stationAudit;

        public Builder(){
            stationAudit = new StationAudit();}

        public Builder recordId(String recordId){
            stationAudit.recordId = recordId;
            return this;
        }

        public Builder operation(String operation){
            stationAudit.operation = operation;
            return this;
        }

        public Builder user(String user){
            stationAudit.user = user;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp){
            stationAudit.timestamp = timestamp;
            return this;
        }

        public StationAudit build() {
            return stationAudit;
        }
    }
}
