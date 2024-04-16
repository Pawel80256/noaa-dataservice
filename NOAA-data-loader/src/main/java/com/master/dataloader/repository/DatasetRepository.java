package com.master.dataloader.repository;

import com.master.dataloader.entities.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset,String> {
}
