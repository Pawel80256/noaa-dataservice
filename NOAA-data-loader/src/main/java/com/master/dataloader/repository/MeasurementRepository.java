package com.master.dataloader.repository;

import com.master.dataloader.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, String> {
    List<Measurement> findAllByStationIdAndDateBetween(String stationId, LocalDate startDate, LocalDate endDate);
}
