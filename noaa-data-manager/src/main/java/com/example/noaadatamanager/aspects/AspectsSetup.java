package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.aspects.updateValidation.UpdateEntityIdValidationAspect;
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
    private final MeasurementRepository measurementRepository;
    private final DataTypeRepository dataTypeRepository;
    private final StationRepository stationRepository;
    private final MeasurementAuditRepository measurementAuditRepository;
    private final StationAuditRepository stationAuditRepository;
    private final LocationRepository locationRepository;
    private final JwtService jwtService;

    public AspectsSetup(RoleRepository roleRepository, MeasurementRepository measurementRepository, DataTypeRepository dataTypeRepository, StationRepository stationRepository, MeasurementAuditRepository measurementAuditRepository, StationAuditRepository stationAuditRepository, LocationRepository locationRepository, JwtService jwtService) {
        this.roleRepository = roleRepository;
        this.measurementRepository = measurementRepository;
        this.dataTypeRepository = dataTypeRepository;
        this.stationRepository = stationRepository;
        this.measurementAuditRepository = measurementAuditRepository;
        this.stationAuditRepository = stationAuditRepository;
        this.locationRepository = locationRepository;
        this.jwtService = jwtService;
    }

    @PostConstruct
    private void setupAspect(){
        Aspects.aspectOf(AuthorizationAspect.class)
                .setRoleRepository(this.roleRepository);
        Aspects.aspectOf(AuthorizationAspect.class)
                .setJwtService(this.jwtService);

        Aspects.aspectOf(MeasurementValidationAspect.class)
                .setNoaaDataRepository(this.measurementRepository);
        Aspects.aspectOf(MeasurementValidationAspect.class)
                .setNoaaDataTypeRepository(this.dataTypeRepository);
        Aspects.aspectOf(MeasurementValidationAspect.class)
                .setNoaaStationRepository(this.stationRepository);

        Aspects.aspectOf(StationValidationAspect.class)
                .setNoaaLocationRepository(this.locationRepository);
        Aspects.aspectOf(StationValidationAspect.class)
                .setNoaaStationRepository(this.stationRepository);
        Aspects.aspectOf(StationValidationAspect.class)
                .setNoaaDataRepository(this.measurementRepository);

        Aspects.aspectOf(MeasurementAuditAspect.class)
                .setMeasurementAuditRepository(this.measurementAuditRepository);
        Aspects.aspectOf(MeasurementAuditAspect.class)
                .setJwtService(this.jwtService);

        Aspects.aspectOf(StationAuditAspect.class)
                .setJwtService(this.jwtService);
        Aspects.aspectOf(StationAuditAspect.class)
                .setStationAuditRepository(this.stationAuditRepository);

        Aspects.aspectOf(UpdateEntityIdValidationAspect.class)
                .setStationRepository(this.stationRepository);
        Aspects.aspectOf(UpdateEntityIdValidationAspect.class)
                .setMeasurementRepository(this.measurementRepository);
    }
}
