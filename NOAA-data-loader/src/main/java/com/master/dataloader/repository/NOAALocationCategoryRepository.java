package com.master.dataloader.repository;

import com.master.dataloader.models.NOAALocationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NOAALocationCategoryRepository extends JpaRepository<NOAALocationCategory,String> {
}
