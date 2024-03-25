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
public class DeleteMeasurementValidationAspect {
    private MeasurementRepository measurementRepository;

    public void setMeasurementRepository(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }
    @Pointcut("execution(* com.example.noaadatamanager.service.MeasurementService.delete(..))")
    public void deleteMethod(){}

    @Before("deleteMethod()")
    public void validateDeleteMethod(JoinPoint joinPoint){
        List<Object> methodArguments = List.of(joinPoint.getArgs());
        if(methodArguments.size() != 1 || !(methodArguments.getFirst() instanceof String)){
            throw new ValidationException(AspectUtils.getMethodMetadata(joinPoint) + " should consume only one argument");
        }

        String measurementId = methodArguments.getFirst().toString();

        if(!measurementRepository.existsById(measurementId)){
            throw new ValidationException("Measurement with id \"" + measurementId + " \" does not exist");
        }
    }
}
