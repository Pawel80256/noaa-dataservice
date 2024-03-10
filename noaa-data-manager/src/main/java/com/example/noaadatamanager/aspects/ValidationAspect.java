package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.exceptions.ValidationException;
import com.example.noaadatamanager.models.NOAAStation;
import com.example.noaadatamanager.repository.NOAADataRepository;
import com.example.noaadatamanager.repository.NOAADataTypeRepository;
import com.example.noaadatamanager.repository.NOAAStationRepository;
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
    private NOAADataTypeRepository noaaDataTypeRepository;
    private NOAAStationRepository noaaStationRepository;

    public void setNoaaDataRepository(NOAADataRepository noaaDataRepository) {
        this.noaaDataRepository = noaaDataRepository;
    }

    public void setNoaaDataTypeRepository(NOAADataTypeRepository noaaDataTypeRepository) {
        this.noaaDataTypeRepository = noaaDataTypeRepository;
    }

    public void setNoaaStationRepository(NOAAStationRepository noaaStationRepository) {
        this.noaaStationRepository = noaaStationRepository;
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

        String measurementId = inputDto.getDataTypeId() + inputDto.getDate().toString() + inputDto.getStationId();
        if(noaaDataRepository.existsById(measurementId)){
            throw new ValidationException("Measurement with id \"" + measurementId +"\" already exists");
        }

        if(!noaaDataTypeRepository.existsById(inputDto.getDataTypeId())){
            throw new ValidationException("Data type with id \"" + inputDto.getDataTypeId() + "\" does not exist");
        }

        if(!noaaStationRepository.existsById(inputDto.getStationId())){
            throw new ValidationException("Station with id \"" + inputDto.getStationId() + "\" does not exist");
        }

        NOAAStation station = noaaStationRepository.findById(inputDto.getStationId()).get();

        if(inputDto.getDate().isAfter(station.getMaxDate()) || inputDto.getDate().isBefore(station.getMinDate())){
            throw new ValidationException("Measurement date for station \"" + inputDto.getStationId() +"\" have to be between " + station.getMinDate() + " - " + station.getMaxDate());
        }
    }

}
