package com.master.dataloader.repository;

import com.master.dataloader.models.NOAADataCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NOAADataCategoryRepository extends JpaRepository<NOAADataCategory, String> {
}
