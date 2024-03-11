package com.example.noaadatamanager.mapper;

import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.models.NOAAStation;
import com.example.noaadatamanager.repository.NOAALocationRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StationMapper {
    private final NOAALocationRepository noaaLocationRepository;

    public StationMapper(NOAALocationRepository noaaLocationRepository) {
        this.noaaLocationRepository = noaaLocationRepository;
    }

    public NOAAStation mapToEntity(StationInputDto input){
        return new NOAAStation.Builder()
                .id(UUID.randomUUID().toString())
                .elevation(input.getElevation())
                .minDate(input.getMinDate())
                .maxDate(input.getMaxDate())
                .latitude(input.getLatitude())
                .longitude(input.getLongitude())
                .name(input.getName())
                .dataCoverage(input.getDataCoverage())
                .elevationUnit(input.getElevationUnit())
                .noaaLocation(noaaLocationRepository.findById(input.getLocationId()).get())
                .source("DATA_MANAGER")
                .build();
    }
}
