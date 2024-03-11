package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.exceptions.ValidationException;
import com.example.noaadatamanager.models.NOAAStation;
import com.example.noaadatamanager.repository.NOAADataRepository;
import com.example.noaadatamanager.repository.NOAADataTypeRepository;
import com.example.noaadatamanager.repository.NOAALocationRepository;
import com.example.noaadatamanager.repository.NOAAStationRepository;
import com.example.noaadatamanager.service.NOAADataService;
import com.example.noaadatamanager.service.NOAAStationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class ValidationAspect {

    private NOAADataRepository noaaDataRepository;
    private NOAADataTypeRepository noaaDataTypeRepository;
    private NOAAStationRepository noaaStationRepository;
    private NOAALocationRepository noaaLocationRepository;

    public void setNoaaDataRepository(NOAADataRepository noaaDataRepository) {
        this.noaaDataRepository = noaaDataRepository;
    }

    public void setNoaaDataTypeRepository(NOAADataTypeRepository noaaDataTypeRepository) {
        this.noaaDataTypeRepository = noaaDataTypeRepository;
    }

    public void setNoaaStationRepository(NOAAStationRepository noaaStationRepository) {
        this.noaaStationRepository = noaaStationRepository;
    }

    public void setNoaaLocationRepository(NOAALocationRepository noaaLocationRepository) {
        this.noaaLocationRepository = noaaLocationRepository;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.*.create(..))")
    public void createMethods(){}

    @Before("createMethods()")
    public void validateCreateMethod(JoinPoint joinPoint){
        String callingServiceName = joinPoint.getSignature().getDeclaringType().getName();
        if(callingServiceName.equals(NOAADataService.class.getName())){
            validateMeasurementInput(joinPoint);
        }
        if(callingServiceName.equals(NOAAStationService.class.getName())){
            validateStationInput(joinPoint);
        }
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.*.delete(..))")
    public void deleteMethods(){}

    @Before("deleteMethods()")
    public void validateDeleteMethod(JoinPoint joinPoint){
        String callingServiceName = joinPoint.getSignature().getDeclaringType().getName();
        if(callingServiceName.equals(NOAADataService.class.getName())){
            validateMeasurementId(joinPoint);
        }
    }

    //todo: validate null fields
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

    private void validateStationInput(JoinPoint joinPoint){
        List<Object> methodArguments = List.of(joinPoint.getArgs());

        if(methodArguments.size() != 1){
            //throw excpetion
        }

        if(!(methodArguments.getFirst() instanceof StationInputDto)){
            //throw exception
        }

        StationInputDto inputDto = (StationInputDto) methodArguments.getFirst();

        if(inputDto == null){
            throw new ValidationException("Station input cannot be null");
        }

        if(inputDto.getName() == null || inputDto.getName().isBlank()){
            throw new ValidationException("Name field is necessary");
        }

        if(inputDto.getLocationId() == null || inputDto.getLocationId().isBlank()){
            throw new ValidationException("Location id field is necessary");
        }

        if(inputDto.getMinDate() == null){
            throw new ValidationException("Min date field is necessary");
        }

        if(inputDto.getMaxDate() == null){
            throw new ValidationException("Max date field is necessary");
        }

        if(!noaaLocationRepository.existsById(inputDto.getLocationId())){
            throw new ValidationException("Location with id \"" + inputDto.getLocationId() + "\" does not exist");
        }

        if(noaaStationRepository.existsByName(inputDto.getName())){
            throw new ValidationException("Station with name \"" + inputDto.getName() +"\" already exist");
        }
    }

    private void validateMeasurementId(JoinPoint joinPoint){
        List<Object> methodArguments = List.of(joinPoint.getArgs());
        if(methodArguments.size() != 1){
            //throw excpetion
        }

        if(!(methodArguments.getFirst() instanceof String)){
            //throw exception
        }

        String measurementId = methodArguments.getFirst().toString();

        if(!noaaDataRepository.existsById(measurementId)){
            throw new ValidationException("Measurement with id \"" + measurementId + " \" does not exist");
        }
    }

}
