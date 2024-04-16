package com.example.noaadatamanager.repository;

import com.example.noaadatamanager.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, String> {
    List<Measurement> findAllByStationIdAndDateBetween(String stationId, LocalDate startDate, LocalDate endDate);

    Integer countByStationId(String stationId);
}
