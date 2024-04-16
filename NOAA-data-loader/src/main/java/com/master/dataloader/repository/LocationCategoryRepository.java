package com.master.dataloader.repository;

import com.master.dataloader.entities.LocationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationCategoryRepository extends JpaRepository<LocationCategory,String> {
}
