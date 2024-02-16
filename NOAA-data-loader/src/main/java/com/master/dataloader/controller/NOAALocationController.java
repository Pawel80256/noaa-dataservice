package com.master.dataloader.controller;

import com.master.dataloader.models.NOAALocation;
import com.master.dataloader.service.NOAALocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/deleteByIds")
    public ResponseEntity<Void> deleteLocationsByIds(@RequestBody List<String> locationsIds){
        noaaLocationService.deleteLocationsByIds(locationsIds);
        return ResponseEntity.ok().build();
    }
}
