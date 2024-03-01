package com.example.noaadatamanager.repository;

import com.example.noaadatamanager.models.NOAALocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NOAALocationRepository extends JpaRepository<NOAALocation, String> {
    List<NOAALocation> findNOAALocationByNoaaLocationCategoryId(String locationCategoryId);
}
