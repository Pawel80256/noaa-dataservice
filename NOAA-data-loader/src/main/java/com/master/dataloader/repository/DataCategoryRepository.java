package com.master.dataloader.repository;

import com.master.dataloader.models.DataCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataCategoryRepository extends JpaRepository<DataCategory, String> {
}
