package com.master.dataloader.service;

import com.master.dataloader.constant.URLs;
import com.master.dataloader.dtos.NOAADataDto;
import com.master.dataloader.models.NOAAData;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.models.NOAAStation;
import com.master.dataloader.repository.NOAADataRepository;
import com.master.dataloader.repository.NOAAStationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NOAADataService {

    private final NOAAStationService noaaStationService;
    private final NOAADataRepository noaaDataRepository;
    private final NOAAStationRepository noaaStationRepository;

    public NOAADataService(NOAAStationService noaaStationService, NOAADataRepository noaaDataRepository, NOAAStationRepository noaaStationRepository) {
        this.noaaStationService = noaaStationService;
        this.noaaDataRepository = noaaDataRepository;
        this.noaaStationRepository = noaaStationRepository;
    }

    public List<NOAADataDto> getAllDtos(String datasetId, String dataTypeId,
                                           String locationId, String stationId, LocalDate startDate, LocalDate endDate) throws Exception {
        List<NOAAData> entities = getAllRemote(datasetId,dataTypeId,locationId,stationId,startDate,endDate);
        List<String> localMeasurementsIds = noaaDataRepository.findAllByStationIdAndDateBetween(stationId,startDate,endDate).stream().map(NOAAData::getId).toList();
        List<NOAADataDto> result = entities.stream().map(NOAADataDto::new).toList();
        result.forEach(measurement -> {
            measurement.setLoaded(localMeasurementsIds.contains(measurement.getId()));
        });
        return result;
    }


    public void load(String datasetId, String dataTypeId, String stationId, LocalDate startDate, LocalDate endDate) throws Exception {
        NOAAStation station = noaaStationRepository.findById(stationId).orElseThrow(()-> new RuntimeException("station not found"));

        //todo: handle when there are no records in timeframe for station -> this can be checked by station.getMaxDate & minDate, compare with parameters
        //todo: obsluzyc case kiedy podczas ladowania danych okaze sie ze datatype jest nieobecny,
        List<NOAAData> dataRecords = getAllRemote(datasetId,dataTypeId,null,stationId,startDate,endDate);

        List<NOAADataType> distinctDataTypesForData = dataRecords.stream().map(NOAAData::getDataType)
                .collect(Collectors.groupingBy(NOAADataType::getId)).values()
                .stream().map(dataTypes -> dataTypes.stream().findFirst().get())
                .collect(Collectors.toList());

        station.setDataTypes(distinctDataTypesForData);

//        if(dataRecords.getCount() <= 1000){
            dataRecords.forEach(d -> d.setSource("NOAA"));
            noaaDataRepository.saveAll(dataRecords);
//        }else {
//            System.out.println("More than 1000 records to load, aborting");
//        }
    }

    private List<NOAAData> getAllRemote(String datasetId, String dataTypeId,
                                        String locationId, String stationId, LocalDate startDate, LocalDate endDate) throws Exception {

        Map<String,Object> requestParams = Utils.getBasicParams();
        requestParams.put("datasetid", datasetId);
        requestParams.put("datatypeid", dataTypeId);
        requestParams.put("locationid", locationId);
        requestParams.put("stationid", stationId);
        requestParams.put("startdate", startDate);
        requestParams.put("enddate", endDate);

        String dataUrl = URLs.baseNoaaApiUrl + URLs.dataUrl;

        List<NOAAData> result = Utils.getRemoteData(dataUrl,requestParams,NOAAData.class);

        result.forEach(measurement -> {
            measurement.setId(measurement.getDataType().getId() + measurement.getDate().toString() + measurement.getStation().getId());
        });

        return result;
    }

}
