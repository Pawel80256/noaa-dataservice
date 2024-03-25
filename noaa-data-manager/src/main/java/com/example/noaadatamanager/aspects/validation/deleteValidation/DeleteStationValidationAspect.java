package com.example.noaadatamanager.aspects.validation.deleteValidation;

import com.example.noaadatamanager.aspects.AspectUtils;
import com.example.noaadatamanager.exceptions.ValidationException;
import com.example.noaadatamanager.repository.DataTypeRepository;
import com.example.noaadatamanager.repository.MeasurementRepository;
import com.example.noaadatamanager.repository.StationRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class DeleteStationValidationAspect {
    private MeasurementRepository measurementRepository;
    private StationRepository stationRepository;

    public void setMeasurementRepository(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public void setStationRepository(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.StationService.delete(..))")
    public void deleteMethod(){}

    @Before("deleteMethod()")
    public void validateStationId(JoinPoint joinPoint){
        List<Object> methodArguments = List.of(joinPoint.getArgs());
        if(methodArguments.size() != 1){
            throw new ValidationException(AspectUtils.getMethodMetadata(joinPoint) + " should consume only one argument");
        }

        if(!(methodArguments.getFirst() instanceof String)){
            throw new ValidationException(
                    AspectUtils.getMethodMetadata(joinPoint) + " should consume one String type argument"
            );
        }

        String stationId = methodArguments.getFirst().toString();

        if(measurementRepository.countByStationId(stationId) > 0){
            throw new ValidationException("Station \"" + stationId + "\" cannot be deleted because it contains measurements");
        }

        if(!stationRepository.existsById(stationId)){
            throw new ValidationException("Station with id \"" + stationId + "\" does not exist");
        }
    }
}
