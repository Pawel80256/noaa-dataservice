package com.example.noaadatamanager.repository;


import com.example.noaadatamanager.models.DataType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataTypeRepository extends JpaRepository<DataType, String> {
}
