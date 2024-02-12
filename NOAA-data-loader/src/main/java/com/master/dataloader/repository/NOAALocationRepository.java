package com.master.dataloader.repository;

import com.master.dataloader.models.NOAALocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NOAALocationRepository extends JpaRepository<NOAALocation, String> {
    List<NOAALocation> findNOAALocationByNoaaLocationCategoryId(String locationCategoryId);
}
