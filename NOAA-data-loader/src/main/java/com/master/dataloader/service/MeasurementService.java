package com.master.dataloader.service;

import com.master.dataloader.constant.URLs;
import com.master.dataloader.dtos.MeasurementDto;
import com.master.dataloader.models.Measurement;
import com.master.dataloader.models.DataType;
import com.master.dataloader.models.Station;
import com.master.dataloader.repository.MeasurementRepository;
import com.master.dataloader.repository.StationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MeasurementService {

    private final StationService stationService;
    private final MeasurementRepository measurementRepository;
    private final StationRepository stationRepository;

    public MeasurementService(StationService stationService, MeasurementRepository measurementRepository, StationRepository stationRepository) {
        this.stationService = stationService;
        this.measurementRepository = measurementRepository;
        this.stationRepository = stationRepository;
    }

    public List<MeasurementDto> getAllDtos(String datasetId, String dataTypeId,
                                           String locationId, String stationId, LocalDate startDate, LocalDate endDate) throws Exception {
        List<Measurement> entities = getAllRemote(datasetId,dataTypeId,locationId,stationId,startDate,endDate);
        List<String> localMeasurementsIds = measurementRepository.findAllByStationIdAndDateBetween(stationId,startDate,endDate).stream().map(Measurement::getId).toList();
        List<MeasurementDto> result = entities.stream().map(MeasurementDto::new).toList();
        result.forEach(measurement -> {
            measurement.setLoaded(localMeasurementsIds.contains(measurement.getId()));
        });
        return result;
    }


    public void load(String datasetId, String dataTypeId, String stationId, LocalDate startDate, LocalDate endDate) throws Exception {
        Station station = stationRepository.findById(stationId).orElseThrow(()-> new RuntimeException("station not found"));

        //todo: handle when there are no records in timeframe for station -> this can be checked by station.getMaxDate & minDate, compare with parameters
        //todo: obsluzyc case kiedy podczas ladowania danych okaze sie ze datatype jest nieobecny,
        List<Measurement> dataRecords = getAllRemote(datasetId,dataTypeId,null,stationId,startDate,endDate);

        List<DataType> distinctDataTypesForData = dataRecords.stream().map(Measurement::getDataType)
                .collect(Collectors.groupingBy(DataType::getId)).values()
                .stream().map(dataTypes -> dataTypes.stream().findFirst().get())
                .collect(Collectors.toList());

        station.setDataTypes(distinctDataTypesForData);

//        if(dataRecords.getCount() <= 1000){
            dataRecords.forEach(d -> d.setSource("NOAA"));
            measurementRepository.saveAll(dataRecords);
//        }else {
//            System.out.println("More than 1000 records to load, aborting");
//        }
    }

    private List<Measurement> getAllRemote(String datasetId, String dataTypeId,
                                           String locationId, String stationId, LocalDate startDate, LocalDate endDate) throws Exception {

        Map<String,Object> requestParams = Utils.getBasicParams();
        requestParams.put("datasetid", datasetId);
        requestParams.put("datatypeid", dataTypeId);
        requestParams.put("locationid", locationId);
        requestParams.put("stationid", stationId);
        requestParams.put("startdate", startDate);
        requestParams.put("enddate", endDate);

        String dataUrl = URLs.baseNoaaApiUrl + URLs.dataUrl;

        List<Measurement> result = Utils.getRemoteData(dataUrl,requestParams, Measurement.class);

        result.forEach(measurement -> {
            measurement.setId(measurement.getDataType().getId() + measurement.getDate().toString() + measurement.getStation().getId());
        });

        return result;
    }

}
