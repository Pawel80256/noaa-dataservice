package com.master.dataloader.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "MEASUREMENTS")
public class Measurement {
    @Id
    private Integer id;
    @ManyToOne
    private Station station;
    private LocalDate date;
    @ManyToOne
    private Element element;
    private Integer value;
    private Character mflag;
    private Character qflag;
    private Character sflag;
}
