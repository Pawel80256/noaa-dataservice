package com.master.dataloader.repository;

import com.master.dataloader.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {
    List<Location> findLocationByLocationCategoryId(String locationCategoryId);
}
