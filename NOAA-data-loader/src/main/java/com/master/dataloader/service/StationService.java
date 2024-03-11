package com.master.dataloader.service;

import com.master.dataloader.constant.URLs;
import com.master.dataloader.dtos.StationDto;
import com.master.dataloader.models.Location;
import com.master.dataloader.models.NOAAStation;
import com.master.dataloader.repository.StationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationDto> getAll() {
        return stationRepository.findAll().stream().map(StationDto::new).toList();
    }

    public List<StationDto> getAllRemoteDtos(String locationId) throws Exception {
        List<NOAAStation> remoteStations = getRemoteByLocationId(locationId);
        remoteStations.forEach(s -> s.setNoaaLocation(new Location(locationId)));
        List<StationDto> result = remoteStations.stream().map(StationDto::new).toList();

        List<String> localStationsIds = stationRepository.findAllById(
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
            station.setNoaaLocation(new Location(locationId));
            station.setSource("NOAA");
        }

        stationRepository.saveAll(stationsToLoad);
    }

    public void deleteByIds(List<String> stationIds) {
        stationRepository.deleteAllById(stationIds);
    }


    private List<NOAAStation> getRemoteByLocationId(String locationId) throws Exception {
        Map<String,Object> requestParams = Utils.getBasicParams();
        requestParams.put("locationid",locationId);

        String stationsUrl = URLs.baseNoaaApiUrl + URLs.stationsUrl;

        return Utils.getRemoteData(stationsUrl,requestParams,NOAAStation.class);
    }
}
