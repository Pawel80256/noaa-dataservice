package com.example.noaadatamanager.repository;

import com.example.noaadatamanager.models.NOAALocationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NOAALocationCategoryRepository extends JpaRepository<NOAALocationCategory,String> {
}
