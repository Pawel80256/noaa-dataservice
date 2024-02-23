package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.dtos.NOAADataTypeDto;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.repository.NOAADataTypeRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NOAADataTypeService {

    private final NOAADataTypeRepository noaaDataTypeRepository;

    public NOAADataTypeService(NOAADataTypeRepository noaaDataTypeRepository) {
        this.noaaDataTypeRepository = noaaDataTypeRepository;
    }

    public List<NOAADataTypeDto> getAll() {
        return noaaDataTypeRepository.findAll().stream().map(NOAADataTypeDto::new).toList();
    }

    public List<NOAADataTypeDto> getAllRemoteDtos() throws Exception {
        List<NOAADataType> remoteDataTypes = getAllRemote();

        List<String> localDatasetIds = noaaDataTypeRepository.findAllById(
                remoteDataTypes.stream().map(NOAADataType::getId).toList()
        ).stream().map(NOAADataType::getId).toList();

        List<NOAADataTypeDto> result = remoteDataTypes.stream().map(NOAADataTypeDto::new).toList();
        result.forEach(dt -> dt.setLoaded(localDatasetIds.contains(dt.getId())));
        return result;
    }

    public void deleteByIds(List<String> ids) {
        noaaDataTypeRepository.deleteAllById(ids);
    }

    public void loadByIds(List<String> dataTypesIds, boolean singly) throws Exception {
        List<NOAADataType> dataTypes = getAllRemote().stream()
                .filter(dataType -> dataTypesIds.contains(dataType.getId()))
                .toList();

        noaaDataTypeRepository.saveAll(dataTypes);
    }

    private List<NOAADataType> getAllRemote() throws Exception {
        List<NOAADataType> result = new ArrayList<>();
        String requestResult;
        JsonNode rootNode, resultsNode;

        //1st part
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("limit", 1000);
        requestParams.put("offset", 1);

        String datasetsUrl = Constants.baseNoaaApiUrl + Constants.dataTypesUrl;
        requestResult = Utils.sendRequest(datasetsUrl, requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        rootNode = mapper.readTree(requestResult);
        resultsNode = rootNode.path("results");

        result.addAll(mapper.readerForListOf(NOAADataType.class).readValue(resultsNode));

        //2nd part
        requestParams.put("offset", 1001);
        requestResult = Utils.sendRequest(datasetsUrl, requestParams);
        resultsNode = mapper.readTree(requestResult).path("results");

        result.addAll(mapper.readerForListOf(NOAADataType.class).readValue(resultsNode));
        return result;

    }
}
