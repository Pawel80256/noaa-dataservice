package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.dtos.update.UpdateDto;
import com.example.noaadatamanager.exceptions.ValidationException;
import com.example.noaadatamanager.repository.MeasurementRepository;
import com.example.noaadatamanager.repository.LocationRepository;
import com.example.noaadatamanager.repository.StationRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class StationValidationAspect {
    private LocationRepository locationRepository;
    private StationRepository stationRepository;
    private MeasurementRepository measurementRepository;

    public void setNoaaLocationRepository(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void setNoaaStationRepository(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void setNoaaDataRepository(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.StationService.create(..))")
    public void createMethod(){}

    @Before("createMethod()")
    public void validateInput(JoinPoint joinPoint){
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

        if(!locationRepository.existsById(inputDto.getLocationId())){
            throw new ValidationException("Location with id \"" + inputDto.getLocationId() + "\" does not exist");
        }

        if(stationRepository.existsByName(inputDto.getName())){
            throw new ValidationException("Station with name \"" + inputDto.getName() +"\" already exist");
        }
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.StationService.delete(..))")
    public void deleteMethod(){}

    @Before("deleteMethod()")
    public void validateStationId(JoinPoint joinPoint){
        List<Object> methodArguments = List.of(joinPoint.getArgs());
        if(methodArguments.size() != 1){
            //throw excpetion
        }

        if(!(methodArguments.getFirst() instanceof String)){
            //throw exception
        }

        String stationId = methodArguments.getFirst().toString();

        if(measurementRepository.countByStationId(stationId) > 0){
            throw new ValidationException("Station \"" + stationId + "\" cannot be deleted because it is used in different resources");
        }

        if(!stationRepository.existsById(stationId)){
            throw new ValidationException("Station with id \"" + stationId + "\" does not exist");
        }
    }

//    @Pointcut("execution(* com.example.noaadatamanager.controller.StationController.*(..))")
//    public void stationControllerMethods(){}
//
//    @Pointcut("args(com.example.noaadatamanager.dtos.update.UpdateDto)")
//    public void updateDtoParameter(){}
//
//    @Before("stationControllerMethods() && updateDtoParameter()")
//    public void validateUpdateMethodParams(JoinPoint joinPoint){
//        var args = joinPoint.getArgs();
//
//        if(args.length != 1 && !(args[0] instanceof UpdateDto)){
//            throw new ValidationException("Incorrect update method parameters");
//        }
//
//        var updateDto = (UpdateDto) args[0];
//
//        if(updateDto.getEntityId() == null || updateDto.getEntityId().toString().isBlank()){
//            throw new ValidationException("Entity id cannot be empty");
//        }
//
//        if(updateDto.getUpdatedFieldValue() == null || updateDto.getUpdatedFieldValue().toString().isBlank()){
//            throw new ValidationException("Updated field value cannot be empty");
//        }
//    }
}
