package com.example.noaadatamanager.service;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.mapper.MeasurementMapper;
import com.example.noaadatamanager.models.NOAAData;
import com.example.noaadatamanager.repository.NOAADataRepository;
import org.springframework.stereotype.Service;

@Service
public class NOAADataService {
    private final NOAADataRepository noaaDataRepository;
    private final MeasurementMapper measurementMapper;
    public NOAADataService(NOAADataRepository noaaDataRepository, MeasurementMapper measurementMapper) {
        this.noaaDataRepository = noaaDataRepository;
        this.measurementMapper = measurementMapper;
    }

    public void create(MeasurementInputDto measurementInputDto){
        NOAAData measurement = measurementMapper.mapToEntity(measurementInputDto);
        noaaDataRepository.save(measurement);
    }

    public void delete(String measurementId){
        noaaDataRepository.deleteById(measurementId);
    }
}
