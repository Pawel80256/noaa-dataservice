package com.example.noaadatamanager.repository.audit;

import com.example.noaadatamanager.models.audit.MeasurementAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementAuditRepository extends JpaRepository<MeasurementAudit, Long> {
}
