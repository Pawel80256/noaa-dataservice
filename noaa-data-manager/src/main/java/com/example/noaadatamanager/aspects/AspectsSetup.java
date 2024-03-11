package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.repository.NOAADataRepository;
import com.example.noaadatamanager.repository.NOAADataTypeRepository;
import com.example.noaadatamanager.repository.NOAAStationRepository;
import com.example.noaadatamanager.repository.RoleRepository;
import com.example.noaadatamanager.repository.audit.MeasurementAuditRepository;
import com.example.noaadatamanager.service.JwtService;
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
    private final JwtService jwtService;

    public AspectsSetup(RoleRepository roleRepository, NOAADataRepository noaaDataRepository, NOAADataTypeRepository noaaDataTypeRepository, NOAAStationRepository noaaStationRepository, MeasurementAuditRepository measurementAuditRepository, JwtService jwtService) {
        this.roleRepository = roleRepository;
        this.noaaDataRepository = noaaDataRepository;
        this.noaaDataTypeRepository = noaaDataTypeRepository;
        this.noaaStationRepository = noaaStationRepository;
        this.measurementAuditRepository = measurementAuditRepository;
        this.jwtService = jwtService;
    }

    @PostConstruct
    private void setupAspect(){
        Aspects.aspectOf(AuthorizationAspect.class)
                .setRoleRepository(this.roleRepository);
        Aspects.aspectOf(AuthorizationAspect.class)
                .setJwtService(this.jwtService);

        Aspects.aspectOf(MeasurementValidationAspect.class)
                .setNoaaDataRepository(this.noaaDataRepository);
        Aspects.aspectOf(MeasurementValidationAspect.class)
                .setNoaaDataTypeRepository(this.noaaDataTypeRepository);
        Aspects.aspectOf(MeasurementValidationAspect.class)
                .setNoaaStationRepository(this.noaaStationRepository);

        Aspects.aspectOf(MeasurementAuditAspect.class)
                .setMeasurementAuditRepository(this.measurementAuditRepository);

        Aspects.aspectOf(MeasurementAuditAspect.class)
                .setJwtService(this.jwtService);
    }
}
