package com.example.noaadatamanager.repository;

import com.example.noaadatamanager.models.NOAADataCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NOAADataCategoryRepository extends JpaRepository<NOAADataCategory, String> {
}
