package com.master.dataloader.repository;

import com.master.dataloader.models.DataType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataTypeRepository extends JpaRepository<DataType, String> {
}
