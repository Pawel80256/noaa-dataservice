package com.example.noaadatamanager.repository;

import com.example.noaadatamanager.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station,String> {
    Boolean existsByName(String name);
}
