package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.exceptions.ValidationException;
import com.example.noaadatamanager.repository.NOAADataRepository;
import com.example.noaadatamanager.service.NOAADataService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Aspect
@Component
public class ValidationAspect {

    private NOAADataRepository noaaDataRepository;

    public void setNoaaDataRepository(NOAADataRepository noaaDataRepository) {
        this.noaaDataRepository = noaaDataRepository;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.*.create(..))")
    public void createMethods(){}

    @Before("createMethods()")
    public void validateInput(JoinPoint joinPoint){
        String callingServiceName = joinPoint.getSignature().getDeclaringType().getName();
        if(callingServiceName.equals(NOAADataService.class.getName())){
            validateMeasurementInput(joinPoint);
        }
    }

    private void validateMeasurementInput(JoinPoint joinPoint){
        List<Object> methodArguments = List.of(joinPoint.getArgs());

        if(methodArguments.size() != 1){
            //throw excpetion
        }

        if(!(methodArguments.getFirst() instanceof MeasurementInputDto)){
            //throw exception
        }

        MeasurementInputDto inputDto = (MeasurementInputDto) methodArguments.getFirst();

        if(inputDto == null){
            throw new RuntimeException("Measurement input data cannot be null");
        }

        //validate existence
        String measurementId = inputDto.getDataTypeId() + inputDto.getDate().toString() + inputDto.getStationId();
        if(noaaDataRepository.existsById(measurementId)){
            throw new ValidationException("Measurement with id \"" + measurementId +"\" already exists");
        }

        
    }

}
