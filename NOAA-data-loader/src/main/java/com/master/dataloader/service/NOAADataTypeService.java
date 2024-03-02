package com.master.dataloader.service;

import com.master.dataloader.constant.URLs;
import com.master.dataloader.dtos.NOAADataTypeDto;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.repository.NOAADataTypeRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

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

    public void loadByIds(List<String> dataTypesIds) throws Exception {
        List<NOAADataType> dataTypes = getAllRemote().stream()
                .filter(dataType -> dataTypesIds.contains(dataType.getId()))
                .toList();

        noaaDataTypeRepository.saveAll(dataTypes);
    }

    private List<NOAADataType> getAllRemote() throws Exception {
        Map<String, Object> requestParams = Utils.getBasicParams();

        String datasetsUrl = URLs.baseNoaaApiUrl + URLs.dataTypesUrl;

        return Utils.getRemoteData(datasetsUrl,requestParams, NOAADataType.class);
    }

}
