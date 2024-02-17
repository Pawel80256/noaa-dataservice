package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
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

    public List<NOAALocation> getAllCountries(){
        return noaaLocationRepository.findNOAALocationByNoaaLocationCategoryId("CNTRY");
    }

    public List<NOAALocation> getAllCities(){
        return noaaLocationRepository.findNOAALocationByNoaaLocationCategoryId("CITY");
    }

    public List<NOAALocation> getAllStates(){
        return noaaLocationRepository.findNOAALocationByNoaaLocationCategoryId("ST");
    }

    public List<NOAALocation> getAllRemoteCountries() throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",1000);
        requestParams.put("offset",1);
        requestParams.put("locationcategoryid", "CNTRY");

        String locationsUrl = Constants.baseNoaaApiUrl + Constants.locationsUrl;
        String requestResult = Utils.sendRequest(locationsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        JsonNode resultsNode = rootNode.path("results");

        List<NOAALocation> locations = mapper.readerForListOf(NOAALocation.class).readValue(resultsNode);

        //check what is faster - stream.foreach() or for(X x :xs)

        for(NOAALocation location : locations){
            location.setNoaaLocationCategory(new NOAALocationCategory("CNTRY"));
        }

        return locations;
    }

    public List<NOAALocation> getAllRemoteCities() throws Exception {
        List<NOAALocation> locations = new ArrayList<>();
        String requestResult;
        JsonNode rootNode, resultsNode;

        //1st part
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",1000);
        requestParams.put("offset",1);
        requestParams.put("locationcategoryid", "CITY");

        String locationsUrl = Constants.baseNoaaApiUrl + Constants.locationsUrl;
        requestResult = Utils.sendRequest(locationsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        rootNode = mapper.readTree(requestResult.toString());
        resultsNode = rootNode.path("results");

        locations.addAll(mapper.readerForListOf(NOAALocation.class).readValue(resultsNode));

        //2nd part
        requestParams.put("offset",1001);
        requestResult = Utils.sendRequest(locationsUrl,requestParams);
        rootNode = mapper.readTree(requestResult.toString());
        resultsNode = rootNode.path("results");
        locations.addAll(mapper.readerForListOf(NOAALocation.class).readValue(resultsNode));

        Iterator<NOAALocation> iterator = locations.iterator();
        while(iterator.hasNext()){
            NOAALocation location = iterator.next();
            String parentCountryId = "FIPS:" + location.getName().substring(location.getName().length()-2);
//            if(noaaLocationRepository.existsById(parentCountryId)){
                location.setNoaaLocationCategory(new NOAALocationCategory("CITY"));
                location.setParent(new NOAALocation(parentCountryId));
//            }
//            else {
//                iterator.remove(); //TODO: inform user
//            }
        }

        filterIncorrectCities(locations);

        return locations;
    }

    private void filterIncorrectCities(List<NOAALocation> cities) throws Exception {
        List<NOAALocation> remoteCountries = getAllRemoteCountries();
        List<String> remoteCountriesIds = remoteCountries.stream()
                .map(NOAALocation::getId)
                .toList();

        Iterator<NOAALocation> cityIterator = cities.iterator();

        while (cityIterator.hasNext()) {
            NOAALocation city = cityIterator.next();
            if (!remoteCountriesIds.contains(city.getParent().getId())) {
                cityIterator.remove();
            }
        }
    }

    public List<NOAALocation> getAllRemoteStates() throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",51);
        requestParams.put("offset",1);
        requestParams.put("locationcategoryid", "ST");

        String locationsUrl = Constants.baseNoaaApiUrl + Constants.locationsUrl;
        String requestResult = Utils.sendRequest(locationsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        JsonNode resultsNode = rootNode.path("results");

        List<NOAALocation> locations = mapper.readerForListOf(NOAALocation.class).readValue(resultsNode);

        //check what is faster - stream.foreach() or for(X x :xs)
        for(NOAALocation location : locations){
            location.setNoaaLocationCategory(new NOAALocationCategory("ST"));
            location.setParent(new NOAALocation("FIPS:US"));
        }

        return locations;
    }

    public void loadAllCountries() throws Exception {
        List<NOAALocation> countries = getAllRemoteCountries();
        noaaLocationRepository.saveAll(countries);
    }

    //new way
    public void loadCountriesByIds(List<String> countriesIds) throws Exception {
        List<NOAALocation> countries = getAllRemoteCountries();

        List<NOAALocation> filteredCountries = countries.stream()
                .filter(country -> countriesIds.contains(country.getId()))
                .toList();

        noaaLocationRepository.saveAll(filteredCountries);
    }

    //todo: require all countries loaded
    public void loadAllCities() throws Exception {
        List<NOAALocation> cities = getAllRemoteCities();
        noaaLocationRepository.saveAll(cities);
    }

    //TODO: w api brakuje kilkunastu krajów (w danych z FTP są), mimo to w miastach są miasta należące do brakujących krajów, powoduje to wywalenie ładowania, na ten moment ominąć, potem obsłużyć
    public void loadCitiesByIds(List<String> citiesIds) throws Exception{
        List<NOAALocation> cities = getAllRemoteCities();

        List<NOAALocation> filteredCities = cities.stream()
                .filter(city -> citiesIds.contains(city.getId()))
                .toList();

        noaaLocationRepository.saveAll(filteredCities);
    }
    //todo: require USA loaded
    public void loadAllStates()throws Exception {
        List<NOAALocation> states = getAllRemoteStates();
        noaaLocationRepository.saveAll(states);
    }

    public void loadStatesByIds(List<String> statesIds) throws Exception {
        List<NOAALocation> states = getAllRemoteStates();

        List<NOAALocation> filteredStates = states.stream()
                .filter(state -> statesIds.contains(state.getId()))
                .toList();

        noaaLocationRepository.saveAll(filteredStates);
    }

    public void deleteLocationsByIds(List<String> locationIds) {
        noaaLocationRepository.deleteAllById(locationIds);
    }
}
