package com.example.noaadatamanager.repository;

import com.example.noaadatamanager.models.NOAAData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NOAADataRepository extends JpaRepository<NOAAData, String> {
    List<NOAAData> findAllByStationIdAndDateBetween(String stationId, LocalDate startDate, LocalDate endDate);
}
