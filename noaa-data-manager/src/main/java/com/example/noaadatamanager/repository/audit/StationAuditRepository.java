package com.example.noaadatamanager.repository.audit;

import com.example.noaadatamanager.models.audit.StationAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationAuditRepository extends JpaRepository<StationAudit, Long> {
}
