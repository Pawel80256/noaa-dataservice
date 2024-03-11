package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.exceptions.ValidationException;
import com.example.noaadatamanager.repository.NOAADataRepository;
import com.example.noaadatamanager.repository.NOAALocationRepository;
import com.example.noaadatamanager.repository.NOAAStationRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class StationValidationAspect {
    private NOAALocationRepository noaaLocationRepository;
    private NOAAStationRepository noaaStationRepository;
    private NOAADataRepository noaaDataRepository;

    public void setNoaaLocationRepository(NOAALocationRepository noaaLocationRepository) {
        this.noaaLocationRepository = noaaLocationRepository;
    }

    public void setNoaaStationRepository(NOAAStationRepository noaaStationRepository) {
        this.noaaStationRepository = noaaStationRepository;
    }

    public void setNoaaDataRepository(NOAADataRepository noaaDataRepository) {
        this.noaaDataRepository = noaaDataRepository;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.NOAAStationService.create(..))")
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

        if(!noaaLocationRepository.existsById(inputDto.getLocationId())){
            throw new ValidationException("Location with id \"" + inputDto.getLocationId() + "\" does not exist");
        }

        if(noaaStationRepository.existsByName(inputDto.getName())){
            throw new ValidationException("Station with name \"" + inputDto.getName() +"\" already exist");
        }
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.NOAAStationService.delete(..))")
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

        if(noaaDataRepository.countByStationId(stationId) > 0){
            throw new ValidationException("Station \"" + stationId + "\" cannot be deleted because it is used in different resources");
        }

        if(!noaaStationRepository.existsById(stationId)){
            throw new ValidationException("Station with id \"" + stationId + "\" does not exist");
        }
    }
}
