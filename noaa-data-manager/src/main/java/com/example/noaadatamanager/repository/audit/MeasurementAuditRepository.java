package com.example.noaadatamanager.repository.audit;

import com.example.noaadatamanager.entities.audit.MeasurementAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementAuditRepository extends JpaRepository<MeasurementAudit, Long> {
}
