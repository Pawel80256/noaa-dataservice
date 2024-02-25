package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dtos.NOAALocationDto;
import com.master.dataloader.models.NOAALocation;
import com.master.dataloader.models.NOAALocationCategory;
import com.master.dataloader.repository.NOAALocationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NOAALocationService {
    private final NOAALocationRepository noaaLocationRepository;

    public NOAALocationService(NOAALocationRepository noaaLocationRepository) {
        this.noaaLocationRepository = noaaLocationRepository;
    }

    public List<NOAALocationDto> getAll(String locationCategoryId){
        return noaaLocationRepository.findNOAALocationByNoaaLocationCategoryId(locationCategoryId)
                .stream().map(NOAALocationDto::new)
                .toList();
    }

    public List<NOAALocationDto> getAllRemoteDtos(String locationCategoryId) throws Exception {
        List<NOAALocation> remoteLocations = getAllRemote(locationCategoryId);

        List<NOAALocationDto> result = remoteLocations.stream().map(NOAALocationDto::new).toList();

        List<String> localLocationsIds = noaaLocationRepository.findAllById(
                remoteLocations.stream().map(NOAALocation::getId).toList()
        ).stream().map(NOAALocation::getId).toList();

        result.forEach(l -> l.setLoaded(localLocationsIds.contains(l.getId())));

        return result;
    }

    public void loadByIds(String locationCategoryId, List<String> locationIds) throws Exception {
        List<NOAALocation> locationsToLoad = getAllRemote(locationCategoryId).stream()
                .filter(l -> locationIds.contains(l.getId()))
                .toList();

        noaaLocationRepository.saveAll(locationsToLoad);
    }

    public void deleteLocationsByIds(List<String> locationIds) {
        noaaLocationRepository.deleteAllById(locationIds);
    }

    private List<NOAALocation> getAllRemote(String locationCategoryId) throws Exception {
        Map<String,Object> requestParams = Utils.getBasicParams();
        requestParams.put("locationcategoryid",locationCategoryId);

        String locationsUrl = Constants.baseNoaaApiUrl + Constants.locationsUrl;

        List<NOAALocation> result =  Utils.getRemoteData(locationsUrl, requestParams, NOAALocation.class);

        result.forEach(l -> l.setNoaaLocationCategory(new NOAALocationCategory(locationCategoryId)));

        if(locationCategoryId.equals("CITY")){
            setParentCountries(result);
            filterIncorrectCities(result);
        }

        return result;
    }

    private void filterIncorrectCities(List<NOAALocation> remoteCities) throws Exception {
        List<String> remoteCountriesIds = getAllRemote("CNTRY").stream()
                .map(NOAALocation::getId)
                .toList();

        Iterator<NOAALocation> cityIterator = remoteCities.iterator();

        while (cityIterator.hasNext()) {
            NOAALocation city = cityIterator.next();
            if (!remoteCountriesIds.contains(city.getParent().getId())) {
                cityIterator.remove();
            }
        }
    }

    private void setParentCountries(List<NOAALocation> cities){
        Iterator<NOAALocation> iterator = cities.iterator();

        while (iterator.hasNext()){
            NOAALocation city = iterator.next();
            String parentCountryId = "FIPS:" + city.getName().substring(city.getName().length()-2);
            city.setParent(new NOAALocation(parentCountryId));
        }
    }
}
