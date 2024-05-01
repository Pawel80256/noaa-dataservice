package com.example.noaadatamanager.aspects.audit;

import com.example.noaadatamanager.repository.audit.MeasurementAuditRepository;
import com.example.noaadatamanager.repository.audit.StationAuditRepository;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {
    private StationAuditRepository stationAuditRepository;
    private MeasurementAuditRepository measurementAuditRepository;

    public void setStationAuditRepository(StationAuditRepository stationAuditRepository) {
        this.stationAuditRepository = stationAuditRepository;
    }

    public void setMeasurementAuditRepository(MeasurementAuditRepository measurementAuditRepository) {
        this.measurementAuditRepository = measurementAuditRepository;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.*.create(..))")
    public void createMethods(){}

    @AfterReturning(pointcut = "createMethods()", returning = "entityId")
    public void addCreateAudit(String entityId){
        System.out.println("testtt: " + entityId);
    }
}
