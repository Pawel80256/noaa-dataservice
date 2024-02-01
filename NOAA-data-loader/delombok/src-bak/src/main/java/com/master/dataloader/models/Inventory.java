package com.master.dataloader.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "INVENTORY")
public class Inventory {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "STATION_ID")
    private Station station;
    @ManyToOne
    @JoinColumn(name = "ELEMENT_CODE")
    private Element element;
    private Integer firstYear;
    private Integer lastYear;

}
