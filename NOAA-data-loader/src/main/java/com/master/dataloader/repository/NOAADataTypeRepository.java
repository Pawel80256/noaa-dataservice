package com.master.dataloader.repository;

import com.master.dataloader.models.NOAADataType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NOAADataTypeRepository extends JpaRepository<NOAADataType, String> {
}
