package com.master.dataloader.service.file_reading;

import com.master.dataloader.models.*;
import com.master.dataloader.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


@Service
@RequiredArgsConstructor
public class DataLoaderService {
    private final ElementReader elementReader = new ElementReader();
    private final MeasurementsReader measurementsReader = new MeasurementsReader();
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final NetworkCodeRepository networkCodeRepository;
    private final ElementRepository elementRepository;
    private final StationRepository stationRepository;
    private final InventoryRepository inventoryRepository;
    private final MeasurementRepository measurementRepository;
    private Map<String, Station> stations;

    public void loadMetadata(){
        Map<String,Country> countries = readCountries();
        Map<String, State> states = readStates();
        Map<Character, NetworkCode> networkCodes = readNetworkCodes();
        Map<String,Element> elements = elementReader.readElements();

        countryRepository.saveAll(countries.values());
        stateRepository.saveAll(states.values());
        networkCodeRepository.saveAll(networkCodes.values());
        elementRepository.saveAll(elements.values());
    }

    public void loadStationData(){
        Map<String,Station> stations = readStations();
        stationRepository.saveAll(stations.values());
        this.stations = stations;
    }

    public void loadInventoryData(){
        Map<String, Station> tempStations = null;
        tempStations = Objects.requireNonNullElseGet(this.stations, this::readStations);
        List<Inventory> inventories = readInventory(tempStations);
        inventoryRepository.saveAll(inventories);
    }

    public void loadMeasurements(String filename){
        Map<String,Element> elements = elementReader.readElements();
        Station station = stationRepository.findById(filename.substring(3,11)).get();
        List<Measurement> measurements = measurementsReader.readMeasurements(filename,elements,station);
        measurementRepository.saveAll(measurements);
    }

    private Map<String,Country> readCountries(){
        Map<String,Country> result = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data/ghcnd-countries.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] countryData = line.split("\\s+");
                Country country = new Country(countryData[0], countryData[1]);
                result.put(countryData[0], country);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private Map<String,State> readStates(){
        Map<String,State> result = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data/ghcnd-states.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] stateData = line.split("\\s+");
                State state = new State(stateData[0], stateData[1]);
                result.put(stateData[0], state);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private Map<Character, NetworkCode> readNetworkCodes(){
        Map<Character, NetworkCode> result = new HashMap<>();

        try(BufferedReader reader = Files.newBufferedReader(Paths.get("data/network-codes.txt"))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] networkCodeData = line.split("=");
                NetworkCode networkCode = new NetworkCode(
                        networkCodeData[0].trim().charAt(0),
                        networkCodeData[1].trim()
                );
                result.put(networkCodeData[0].trim().charAt(0), networkCode);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private Map<String,Station> readStations(){
        Map<String,Station> result = new HashMap<>();

        Map<String,Country> countriess = getAllCountriesAsMap();
        Map<Character,NetworkCode> networkCodess = getAllNetworkCodesAsMap();
        Map<String, State> statess = getAllStatesAsMap();

        try(BufferedReader reader = Files.newBufferedReader(Paths.get("data/ghcnd-stations.txt"))){
            String line;
            while((line = reader.readLine()) != null){
                if(line.length()!=85){
                    line = String.format("%-" + 85 + "s", line);
                }
                Station station = Station.builder()
                        .id(line.substring(3,11))
                        .country(countriess.get(line.substring(0,2)))
                        .networkCode(networkCodess.get(line.substring(2,3).charAt(0)))
                        .state(statess.get(line.substring(38,40)))
                        .latitude(Double.valueOf(line.substring(12,20)))
                        .longitude(Double.valueOf(line.substring(21,30)))
                        .elevation(Double.valueOf(line.substring(31,37)))
                        .name(line.substring(41,71))
                        .gsnFlag(line.substring(72,75))
                        .hcnCrnFlag(line.substring(76,79))
                        .wmoId(line.substring(80,85))
                        .build();
                result.put(line.substring(3,11),station);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return result;
    }

    private List<Inventory> readInventory(Map<String,Station> stations){
        List<Inventory> result = new ArrayList<>();
        Map<String,Element> elements = getAllElementsAsMap();
        try(BufferedReader reader = Files.newBufferedReader(Paths.get("data/ghcnd-inventory.txt"))){
            String line;
            Integer id = 1;
            while ((line = reader.readLine()) != null) {
                if(line.length()!=45){
                    line = String.format("%-" + 85 + "s", line);
                }

                Inventory inventory = Inventory.builder()
                        .id(id)
                        .station(stations.get(line.substring(3,11)))
                        .element(elements.get(line.substring(31,35)))
                        .firstYear(tryParseInt(line.substring(36,40).trim()))
                        .lastYear(tryParseInt(line.substring(41,45).trim()))
                        .build();
                id++;

                result.add(inventory);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return result;
    }

    //support
    private Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Map<String,Country> getAllCountriesAsMap(){
        Map<String,Country> result = new HashMap<>();
        List<Country> countries = countryRepository.findAll();
        countries.forEach(c -> result.put(c.getCode(),c));
        return result;
    }

    private Map<Character, NetworkCode> getAllNetworkCodesAsMap(){
        Map<Character, NetworkCode> result = new HashMap<>();
        List<NetworkCode> networkCodes = networkCodeRepository.findAll();
        networkCodes.forEach(c->result.put(c.getCode(),c));
        return result;
    }

    private Map<String,State> getAllStatesAsMap(){
        Map<String, State> result = new HashMap<>();
        List<State> states = stateRepository.findAll();
        states.forEach(s-> result.put(s.getCode(),s));
        return result;
    }

    private Map<String,Element> getAllElementsAsMap(){
        Map<String,Element> result = new HashMap<>();
        List<Element> elements = elementRepository.findAll();
        elements.forEach(e->result.put(e.getCode(),e));
        return result;
    }

    public void clearDatabase() {
        inventoryRepository.deleteAll();
        stationRepository.deleteAll();
        elementRepository.deleteAll();
        countryRepository.deleteAll();
        stateRepository.deleteAll();
        networkCodeRepository.deleteAll();
    }
}
