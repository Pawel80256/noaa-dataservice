package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAALocation;
import com.master.dataloader.models.NOAALocationCategory;
import com.master.dataloader.repository.NOAALocationRepository;
import com.master.dataloader.service.NOAALocationService;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("NOAA/location")
public class NOAALocationController {

    private final NOAALocationService noaaLocationService;

    public NOAALocationController(NOAALocationService noaaLocationService) {
        this.noaaLocationService = noaaLocationService;
    }

//    @GetMapping
//    public ResponseEntity<PaginationWrapper<NOAALocation>> getAll(
//            @RequestParam(name = "limit") Integer limit,
//            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
//            @RequestParam(name = "datasetId", required = false) String datasetId,
//            @RequestParam(name = "dataCategoryId", required = false) String dataCategoryId,
//            @RequestParam(name = "locationCategoryId", required = false) String locationCategoryId,
//            @RequestParam(name = "startDate", required = false) LocalDate startDate,
//            @RequestParam(name = "endDate", required = false) LocalDate endDate
//            ) throws Exception {
//
//        return ResponseEntity.ok(
//                noaaLocationService.getAll(limit,offset,datasetId,dataCategoryId,locationCategoryId,startDate,endDate)
//        );
//    }
    @GetMapping("/countries")
    public ResponseEntity<List<NOAALocation>> getAllCountries(){
        return ResponseEntity.ok(noaaLocationService.getAllCountries());
    }

    @GetMapping("/cities")
    public ResponseEntity<List<NOAALocation>> getAllCities(){
        return ResponseEntity.ok(noaaLocationService.getAllCities());
    }

    @GetMapping("/states")
    public ResponseEntity<List<NOAALocation>> getAllStates(){
        return ResponseEntity.ok(noaaLocationService.getAllStates());
    }
    @GetMapping("/countries/remote")
    public ResponseEntity<List<NOAALocation>> getAllRemoteCountries() throws Exception {
        return ResponseEntity.ok(noaaLocationService.getAllRemoteCountries());
    }

    @GetMapping("/cities/remote")
    public ResponseEntity<List<NOAALocation>> getAllRemoteCities() throws Exception {
        return ResponseEntity.ok(noaaLocationService.getAllRemoteCities());
    }

    @GetMapping("/states/remote")
    public ResponseEntity<List<NOAALocation>> getAllRemoteStates() throws Exception{
        return ResponseEntity.ok(noaaLocationService.getAllRemoteStates());
    }

    @PutMapping("/countries/load")
    public ResponseEntity<Void> loadAllCountries() throws Exception {
        noaaLocationService.loadAllCountries();
        return ResponseEntity.ok().build();
    }

    @PutMapping("countries/loadByIds")
    public ResponseEntity<Void> loadCountriesByIds(@RequestBody List<String> countriesIds) throws Exception {
        noaaLocationService.loadCountriesByIds(countriesIds);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/cities/load")
    public ResponseEntity<Void> loadAllCities() throws Exception {
        noaaLocationService.loadAllCities();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cities/loadByIds")
    public ResponseEntity<Void> loadCitiesByIds(@RequestBody List<String> citiesIds) throws Exception{
        noaaLocationService.loadCitiesByIds(citiesIds);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/states/load")
    public ResponseEntity<Void> loadAllStates() throws Exception{
        noaaLocationService.loadAllStates();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/states/loadByIds")
    public ResponseEntity<Void> loadStatesByIds(@RequestBody List<String> statesIds) throws Exception {
        noaaLocationService.loadStatesByIds(statesIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/countries")
    public ResponseEntity<Void> deleteCountriesByIds(@RequestBody List<String> countriesIds){
        noaaLocationService.deleteCountriesByIds(countriesIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cities")
    public ResponseEntity<Void> deleteCitiesByIds(@RequestBody List<String> citiesIds){
        noaaLocationService.deleteCitiesIds(citiesIds);
        return ResponseEntity.ok().build();
    }
}
