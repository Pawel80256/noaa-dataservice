package com.example.noaadatamanager.aspects.validation.updateValidation;

import com.example.noaadatamanager.dtos.update.interfaces.UpdateDto;
import com.example.noaadatamanager.exceptions.ValidationException;
import com.example.noaadatamanager.repository.MeasurementRepository;
import com.example.noaadatamanager.repository.StationRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UpdateEntityIdValidationAspect {
    private MeasurementRepository measurementRepository;
    private StationRepository stationRepository;

    public void setMeasurementRepository(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public void setStationRepository(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Pointcut("args(com.example.noaadatamanager.dtos.update.interfaces.UpdateDto)")
    public void updateDtoParameter(){}

    @Pointcut("execution(* com.example.noaadatamanager.controller.StationController.*(..))")
    public void stationControllerMethods(){}

    @Pointcut("execution(* com.example.noaadatamanager.controller.MeasurementController.*(..))")
    public void measurementControllerMethods(){}

    @Before("updateDtoParameter() && stationControllerMethods()")
    public void validateStationUpdate(JoinPoint joinPoint){
        var updateDto = (UpdateDto) joinPoint.getArgs()[0];
        if(!stationRepository.existsById(updateDto.getEntityId().toString())){
            throw new ValidationException("Station with id \"" + updateDto.getEntityId() + "\" does not exist");
        }
    }

    @Before("updateDtoParameter() && measurementControllerMethods()")
    public void validateMeasurementUpdate(JoinPoint joinPoint){
        var updateDto = (UpdateDto) joinPoint.getArgs()[0];
        if(!measurementRepository.existsById(updateDto.getEntityId().toString())){
            throw new ValidationException("Measurement with id \"" + updateDto.getEntityId() + "\" does not exist");
        }
    }
}
