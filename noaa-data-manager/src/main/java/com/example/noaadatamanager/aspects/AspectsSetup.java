package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.repository.*;
import com.example.noaadatamanager.repository.audit.MeasurementAuditRepository;
import com.example.noaadatamanager.repository.audit.StationAuditRepository;
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
    private final StationAuditRepository stationAuditRepository;
    private final NOAALocationRepository noaaLocationRepository;
    private final JwtService jwtService;

    public AspectsSetup(RoleRepository roleRepository, NOAADataRepository noaaDataRepository, NOAADataTypeRepository noaaDataTypeRepository, NOAAStationRepository noaaStationRepository, MeasurementAuditRepository measurementAuditRepository, StationAuditRepository stationAuditRepository, NOAALocationRepository noaaLocationRepository, JwtService jwtService) {
        this.roleRepository = roleRepository;
        this.noaaDataRepository = noaaDataRepository;
        this.noaaDataTypeRepository = noaaDataTypeRepository;
        this.noaaStationRepository = noaaStationRepository;
        this.measurementAuditRepository = measurementAuditRepository;
        this.stationAuditRepository = stationAuditRepository;
        this.noaaLocationRepository = noaaLocationRepository;
        this.jwtService = jwtService;
    }

    @PostConstruct
    private void setupAspect(){
        Aspects.aspectOf(AuthorizationAspect.class)
                .setRoleRepository(this.roleRepository);
        Aspects.aspectOf(AuthorizationAspect.class)
                .setJwtService(this.jwtService);

        Aspects.aspectOf(ValidationAspect.class)
                .setNoaaDataRepository(this.noaaDataRepository);
        Aspects.aspectOf(ValidationAspect.class)
                .setNoaaDataTypeRepository(this.noaaDataTypeRepository);
        Aspects.aspectOf(ValidationAspect.class)
                .setNoaaStationRepository(this.noaaStationRepository);
        Aspects.aspectOf(ValidationAspect.class)
                .setNoaaLocationRepository(this.noaaLocationRepository);

        Aspects.aspectOf(MeasurementAuditAspect.class)
                .setMeasurementAuditRepository(this.measurementAuditRepository);
        Aspects.aspectOf(MeasurementAuditAspect.class)
                .setJwtService(this.jwtService);

        Aspects.aspectOf(StationAuditAspect.class)
                .setJwtService(this.jwtService);
        Aspects.aspectOf(StationAuditAspect.class)
                .setStationAuditRepository(this.stationAuditRepository);
    }
}
