package com.example.noaadatamanager.mapper;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.models.NOAAData;
import com.example.noaadatamanager.models.NOAAStation;
import com.example.noaadatamanager.repository.NOAADataTypeRepository;
import com.example.noaadatamanager.repository.NOAAStationRepository;
import org.springframework.stereotype.Service;

@Service
public class MeasurementMapper {
    private final NOAAStationRepository stationRepository;
    private final NOAADataTypeRepository dataTypeRepository;

    public MeasurementMapper(NOAAStationRepository stationRepository, NOAADataTypeRepository dataTypeRepository) {
        this.stationRepository = stationRepository;
        this.dataTypeRepository = dataTypeRepository;
    }

    public NOAAData mapToEntity(MeasurementInputDto input){
        return new NOAAData.Builder()
                .id(input.getDataTypeId() + input.getDate().toString() + input.getStationId())
                .dataType(dataTypeRepository.findById(input.getDataTypeId()).get())
                .station(stationRepository.findById(input.getStationId()).get())
                .date(input.getDate())
                .value(input.getValue())
                .source("DATA_MANAGER")
                .build();
    }
}
