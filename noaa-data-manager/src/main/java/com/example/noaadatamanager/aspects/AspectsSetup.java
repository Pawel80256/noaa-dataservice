package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.repository.NOAADataRepository;
import com.example.noaadatamanager.repository.NOAADataTypeRepository;
import com.example.noaadatamanager.repository.NOAAStationRepository;
import com.example.noaadatamanager.repository.RoleRepository;
import com.example.noaadatamanager.repository.audit.MeasurementAuditRepository;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.Aspects;
import org.springframework.stereotype.Component;

@Component
public class AspectsSetup {
    private final RoleRepository roleRepository;
    private final NOAADataRepository noaaDataRepository;
    private final NOAADataTypeRepository noaaDataTypeRepository;
    private final NOAAStationRepository noaaStationRepository;
    private final MeasurementAuditRepository measurementAuditRepository;

    public AspectsSetup(RoleRepository roleRepository, NOAADataRepository noaaDataRepository, NOAADataTypeRepository noaaDataTypeRepository, NOAAStationRepository noaaStationRepository, MeasurementAuditRepository measurementAuditRepository) {
        this.roleRepository = roleRepository;
        this.noaaDataRepository = noaaDataRepository;
        this.noaaDataTypeRepository = noaaDataTypeRepository;
        this.noaaStationRepository = noaaStationRepository;
        this.measurementAuditRepository = measurementAuditRepository;
    }

    @PostConstruct
    private void setupAspect(){
        Aspects.aspectOf(AuthorizationAspect.class)
                .setRoleRepository(this.roleRepository);

        Aspects.aspectOf(ValidationAspect.class)
                .setNoaaDataRepository(this.noaaDataRepository);
        Aspects.aspectOf(ValidationAspect.class)
                .setNoaaDataTypeRepository(this.noaaDataTypeRepository);
        Aspects.aspectOf(ValidationAspect.class)
                .setNoaaStationRepository(this.noaaStationRepository);
        Aspects.aspectOf(AuditAspect.class)
                .setMeasurementAuditRepository(this.measurementAuditRepository);
    }
}
