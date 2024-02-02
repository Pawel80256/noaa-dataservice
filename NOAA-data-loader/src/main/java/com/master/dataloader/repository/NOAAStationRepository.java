package com.master.dataloader.repository;

import com.master.dataloader.models.NOAALocation;
import com.master.dataloader.models.NOAAStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NOAAStationRepository extends JpaRepository<NOAAStation,String> {
}
