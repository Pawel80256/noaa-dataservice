package com.example.noaadatamanager.service;

import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.dtos.request.StationUpdateNameDto;
import com.example.noaadatamanager.exceptions.ValidationException;
import com.example.noaadatamanager.mapper.StationMapper;
import com.example.noaadatamanager.models.Station;
import com.example.noaadatamanager.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StationService {
    private final StationRepository stationRepository;
    private final StationMapper stationMapper;

    public StationService(StationRepository stationRepository, StationMapper stationMapper) {
        this.stationRepository = stationRepository;
        this.stationMapper = stationMapper;
    }

    public String create(StationInputDto stationInputDto){
        Station station = stationMapper.mapToEntity(stationInputDto);
        stationRepository.save(station);
        return station.getId();
    }

    public void delete(String stationId){
        stationRepository.deleteById(stationId);
    }

    public void updateName(StationUpdateNameDto dto){
        Optional<Station> optionalStation = stationRepository.findById(dto.getEntityId()); //moge tak zrobic bo aspekt waliduje id
        if(optionalStation.isEmpty()){
            throw new ValidationException("Station with id " + dto.getEntityId() + " does not exist");
        }
        Station station = optionalStation.get();
        station.setName(dto.getUpdatedFieldValue());
        stationRepository.save(station);
    }
}
