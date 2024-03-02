package com.master.dataloader.service;

import com.master.dataloader.constant.URLs;
import com.master.dataloader.dtos.NOAAStationDto;
import com.master.dataloader.models.NOAALocation;
import com.master.dataloader.models.NOAAStation;
import com.master.dataloader.repository.NOAAStationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NOAAStationService {

    private final NOAAStationRepository noaaStationRepository;

    public NOAAStationService(NOAAStationRepository noaaStationRepository) {
        this.noaaStationRepository = noaaStationRepository;
    }

    public List<NOAAStationDto> getAll() {
        return noaaStationRepository.findAll().stream().map(NOAAStationDto::new).toList();
    }

    public List<NOAAStationDto> getAllRemoteDtos(String locationId) throws Exception {
        List<NOAAStation> remoteStations = getRemoteByLocationId(locationId);
        remoteStations.forEach(s -> s.setNoaaLocation(new NOAALocation(locationId)));
        List<NOAAStationDto> result = remoteStations.stream().map(NOAAStationDto::new).toList();

        List<String> localStationsIds = noaaStationRepository.findAllById(
                remoteStations.stream().map(NOAAStation::getId).toList()
        ).stream().map(NOAAStation::getId).toList();

        result.forEach(s -> s.setLoaded(localStationsIds.contains(s.getId())));

        return result;
    }

    public void loadByIds(String locationId, List<String> stationIds) throws Exception {
        List<NOAAStation> remoteByLocationId = getRemoteByLocationId(locationId);
        List<NOAAStation> stationsToLoad = remoteByLocationId.stream()
                .filter(station -> stationIds.contains(station.getId())).toList();

        for(NOAAStation station : stationsToLoad){
            station.setNoaaLocation(new NOAALocation(locationId));
            station.setSource("NOAA");
        }

        noaaStationRepository.saveAll(stationsToLoad);
    }

    public void deleteByIds(List<String> stationIds) {
        noaaStationRepository.deleteAllById(stationIds);
    }


    private List<NOAAStation> getRemoteByLocationId(String locationId) throws Exception {
        Map<String,Object> requestParams = Utils.getBasicParams();
        requestParams.put("locationid",locationId);

        String stationsUrl = URLs.baseNoaaApiUrl + URLs.stationsUrl;

        return Utils.getRemoteData(stationsUrl,requestParams,NOAAStation.class);
    }
}
