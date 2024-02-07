package com.master.dataloader.repository;

import com.master.dataloader.models.NOAADataset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NOAADatasetRepository extends JpaRepository<NOAADataset,String> {
}
