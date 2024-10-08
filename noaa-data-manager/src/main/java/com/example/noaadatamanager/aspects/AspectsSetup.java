package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.aspects.audit.AuditAspect;
import com.example.noaadatamanager.aspects.authorization.AuthorizationAspect;
import com.example.noaadatamanager.aspects.validation.createValidation.CreateMeasurementValidationAspect;
import com.example.noaadatamanager.aspects.validation.createValidation.CreateStationValidationAspect;
import com.example.noaadatamanager.aspects.validation.deleteValidation.DeleteMeasurementValidationAspect;
import com.example.noaadatamanager.aspects.validation.deleteValidation.DeleteStationValidationAspect;
import com.example.noaadatamanager.aspects.validation.updateValidation.UpdateEntityIdValidationAspect;
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

        Aspects.aspectOf(CreateMeasurementValidationAspect.class)
                .setNoaaDataRepository(this.measurementRepository);
        Aspects.aspectOf(CreateMeasurementValidationAspect.class)
                .setNoaaDataTypeRepository(this.dataTypeRepository);
        Aspects.aspectOf(CreateMeasurementValidationAspect.class)
                .setNoaaStationRepository(this.stationRepository);

        Aspects.aspectOf(CreateStationValidationAspect.class)
                .setNoaaLocationRepository(this.locationRepository);
        Aspects.aspectOf(CreateStationValidationAspect.class)
                .setNoaaStationRepository(this.stationRepository);
        Aspects.aspectOf(CreateStationValidationAspect.class)
                .setNoaaDataRepository(this.measurementRepository);

        Aspects.aspectOf(UpdateEntityIdValidationAspect.class)
                .setStationRepository(this.stationRepository);
        Aspects.aspectOf(UpdateEntityIdValidationAspect.class)
                .setMeasurementRepository(this.measurementRepository);

        Aspects.aspectOf(DeleteStationValidationAspect.class)
                .setMeasurementRepository(this.measurementRepository);
        Aspects.aspectOf(DeleteStationValidationAspect.class)
                .setStationRepository(this.stationRepository);

        Aspects.aspectOf(DeleteMeasurementValidationAspect.class)
                .setMeasurementRepository(this.measurementRepository);

        Aspects.aspectOf(AuditAspect.class)
                .setMeasurementAuditRepository(measurementAuditRepository);
        Aspects.aspectOf(AuditAspect.class)
                .setStationAuditRepository(stationAuditRepository);
        Aspects.aspectOf(AuditAspect.class)
                .setJwtService(jwtService);

    }
}
