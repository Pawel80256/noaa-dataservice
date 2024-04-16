package com.example.noaadatamanager.entities.audit;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity

@Table(schema = "audit", name = "measurement_audit")
public class MeasurementAudit {
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
        private MeasurementAudit measurementAudit;

        public Builder(){measurementAudit = new MeasurementAudit();}

        public Builder recordId(String recordId){
            measurementAudit.recordId = recordId;
            return this;
        }

        public Builder operation(String operation){
            measurementAudit.operation = operation;
            return this;
        }

        public Builder user(String user){
            measurementAudit.user = user;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp){
            measurementAudit.timestamp = timestamp;
            return this;
        }

        public MeasurementAudit build() {
            return measurementAudit;
        }
    }

    @Override
    public String toString() {
        return "MeasurementAudit{" +
                "id=" + id +
                ", recordId='" + recordId + '\'' +
                ", operation='" + operation + '\'' +
                ", user='" + user + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
