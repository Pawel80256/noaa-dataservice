package com.example.noaadatamanager.repository;

import com.example.noaadatamanager.models.NOAAStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<NOAAStation,String> {
    Boolean existsByName(String name);
}
