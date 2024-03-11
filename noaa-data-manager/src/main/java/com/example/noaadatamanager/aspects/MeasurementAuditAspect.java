package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.models.audit.MeasurementAudit;
import com.example.noaadatamanager.repository.audit.MeasurementAuditRepository;
import com.example.noaadatamanager.repository.audit.StationAuditRepository;
import com.example.noaadatamanager.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Aspect
@Component
public class MeasurementAuditAspect {
    private MeasurementAuditRepository measurementAuditRepository;
    private JwtService jwtService;

    public void setMeasurementAuditRepository(MeasurementAuditRepository measurementAuditRepository) {
        this.measurementAuditRepository = measurementAuditRepository;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.NOAADataService.create(..))")
    public void createMeasurement(){}

    @After("createMeasurement()")
    public void addCreateAudit(JoinPoint joinPoint){
        MeasurementInputDto inputDto = (MeasurementInputDto) List.of(joinPoint.getArgs()).getFirst();
        String measurementId = inputDto.getDataTypeId() + inputDto.getDate().toString() + inputDto.getStationId();

        MeasurementAudit measurementAudit = new MeasurementAudit.Builder()
                .recordId(measurementId)
                .operation("CREATE")
                .timestamp(LocalDateTime.now())
                .user(jwtService.getSubFromToken(extractTokenFromHeader()))
                .build();

        measurementAuditRepository.save(measurementAudit);
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.NOAADataService.delete(..))")
    public void deleteMeasurement(){}

    @After("deleteMeasurement()")
    public void addDeleteAudit(JoinPoint joinPoint){
        String measurementId = joinPoint.getArgs()[0].toString();

        MeasurementAudit measurementAudit = new MeasurementAudit.Builder()
                .recordId(measurementId)
                .operation("DELETE")
                .timestamp(LocalDateTime.now())
                .user(jwtService.getSubFromToken(extractTokenFromHeader()))
                .build();

        measurementAuditRepository.save(measurementAudit);
    }

    private String extractTokenFromHeader(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        return authorizationHeader.substring(7);
    }
}
