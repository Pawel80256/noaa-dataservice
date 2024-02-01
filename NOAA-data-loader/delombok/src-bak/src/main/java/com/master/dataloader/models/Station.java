package com.master.dataloader.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table (name = "STATIONS")
public class Station {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "COUNTRY_CODE")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "NETWORK_CODE")
    private NetworkCode networkCode;

    private Double latitude;

    private Double longitude;

    private Double elevation;

    @ManyToOne
    @JoinColumn(name = "STATE_CODE")
    private State state;

    private String name;

    private String gsnFlag;

    private String hcnCrnFlag;

    private String wmoId;
}
