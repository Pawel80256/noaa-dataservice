package com.example.noaadatamanager.mapper;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.models.Measurement;
import com.example.noaadatamanager.repository.DataTypeRepository;
import com.example.noaadatamanager.repository.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class MeasurementMapper {
    private final StationRepository stationRepository;
    private final DataTypeRepository dataTypeRepository;

    public MeasurementMapper(StationRepository stationRepository, DataTypeRepository dataTypeRepository) {
        this.stationRepository = stationRepository;
        this.dataTypeRepository = dataTypeRepository;
    }

    public Measurement mapToEntity(MeasurementInputDto input){
        return new Measurement.Builder()
                .id(input.getDataTypeId() + input.getDate().toString() + input.getStationId())
                .dataType(dataTypeRepository.findById(input.getDataTypeId()).get())
                .station(stationRepository.findById(input.getStationId()).get())
                .date(input.getDate())
                .value(input.getValue())
                .source("DATA_MANAGER")
                .build();
    }
}
