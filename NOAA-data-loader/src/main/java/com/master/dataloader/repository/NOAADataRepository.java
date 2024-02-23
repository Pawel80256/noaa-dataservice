package com.master.dataloader.repository;

import com.master.dataloader.models.NOAAData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NOAADataRepository extends JpaRepository<NOAAData, String> {
}
