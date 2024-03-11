package com.master.dataloader.service;

import com.master.dataloader.constant.URLs;
import com.master.dataloader.dtos.LocationDto;
import com.master.dataloader.models.Location;
import com.master.dataloader.models.LocationCategory;
import com.master.dataloader.repository.LocationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationDto> getAll(String locationCategoryId){
        return locationRepository.findLocationByLocationCategoryId(locationCategoryId)
                .stream().map(LocationDto::new)
                .toList();
    }

    public List<LocationDto> getAllRemoteDtos(String locationCategoryId) throws Exception {
        List<Location> remoteLocations = getAllRemote(locationCategoryId);

        List<LocationDto> result = remoteLocations.stream().map(LocationDto::new).toList();

        List<String> localLocationsIds = locationRepository.findAllById(
                remoteLocations.stream().map(Location::getId).toList()
        ).stream().map(Location::getId).toList();

        result.forEach(l -> l.setLoaded(localLocationsIds.contains(l.getId())));

        return result;
    }

    public void loadByIds(String locationCategoryId, List<String> locationIds) throws Exception {
            List<Location> locationsToLoad = getAllRemote(locationCategoryId).stream()
                    .filter(l -> locationIds.contains(l.getId()))
                    .toList();

            locationRepository.saveAll(locationsToLoad);
    }

    public void deleteLocationsByIds(List<String> locationIds) {
            locationRepository.deleteAllById(locationIds);
    }

    private List<Location> getAllRemote(String locationCategoryId) throws Exception {
        Map<String,Object> requestParams = Utils.getBasicParams();
        requestParams.put("locationcategoryid",locationCategoryId);

        String locationsUrl = URLs.baseNoaaApiUrl + URLs.locationsUrl;

        List<Location> result =  Utils.getRemoteData(locationsUrl, requestParams, Location.class);

        result.forEach(l -> l.setNoaaLocationCategory(new LocationCategory(locationCategoryId)));

        if(locationCategoryId.equals("CITY")){
            setParentCountries(result);
            filterIncorrectCities(result);
        }
        if(locationCategoryId.equals("ST")){
            result.forEach(l -> l.setParent(new Location("FIPS:US")));
        }

        return result;
    }

    private void filterIncorrectCities(List<Location> remoteCities) throws Exception {
        List<String> remoteCountriesIds = getAllRemote("CNTRY").stream()
                .map(Location::getId)
                .toList();

        Iterator<Location> cityIterator = remoteCities.iterator();

        while (cityIterator.hasNext()) {
            Location city = cityIterator.next();
            if (!remoteCountriesIds.contains(city.getParent().getId())) {
                cityIterator.remove();
            }
        }
    }

    private void setParentCountries(List<Location> cities){
        Iterator<Location> iterator = cities.iterator();

        while (iterator.hasNext()){
            Location city = iterator.next();
            String parentCountryId = "FIPS:" + city.getName().substring(city.getName().length()-2);
            city.setParent(new Location(parentCountryId));
        }
    }
}
