package com.master.dataloader.service;

import com.master.dataloader.constant.URLs;
import com.master.dataloader.dtos.StationDto;
import com.master.dataloader.entities.Location;
import com.master.dataloader.entities.Station;
import com.master.dataloader.repository.StationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationDto> getAllLocal() {
        return stationRepository.findAll().stream().map(StationDto::new).toList();
    }

    public List<StationDto> getAllRemote(String locationId) throws IOException {
        List<Station> remoteStations = getRemoteByLocationId(locationId);
        remoteStations.forEach(s -> s.setNoaaLocation(new Location(locationId)));
        List<StationDto> result = remoteStations.stream().map(StationDto::new).toList();

        List<String> localStationsIds = stationRepository.findAllById(
                remoteStations.stream().map(Station::getId).toList()
        ).stream().map(Station::getId).toList();

        result.forEach(s -> s.setLoaded(localStationsIds.contains(s.getId())));

        return result;
    }

    public void loadByIds(String locationId, List<String> stationIds) throws IOException {
        List<Station> remoteByLocationId = getRemoteByLocationId(locationId);
        List<Station> stationsToLoad = remoteByLocationId.stream()
                .filter(station -> stationIds.contains(station.getId())).toList();

        for(Station station : stationsToLoad){
            station.setNoaaLocation(new Location(locationId));
            station.setSource("NOAA");
        }

        stationRepository.saveAll(stationsToLoad);
    }

    public void deleteByIds(List<String> stationIds) {
        stationRepository.deleteAllById(stationIds);
    }


    private List<Station> getRemoteByLocationId(String locationId) throws IOException {
        Map<String,Object> requestParams = Utils.getBasicParams();
        requestParams.put("locationid",locationId);

        String stationsUrl = URLs.baseNoaaApiUrl + URLs.stationsUrl;

        return Utils.getRemoteData(stationsUrl,requestParams, Station.class);
    }
}
