package com.example.noaadatamanager.repository;


import com.example.noaadatamanager.models.NOAADataType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NOAADataTypeRepository extends JpaRepository<NOAADataType, String> {
}
