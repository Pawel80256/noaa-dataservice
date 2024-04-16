package com.master.dataloader.service;

import com.master.dataloader.constant.URLs;
import com.master.dataloader.dtos.DataTypeDto;
import com.master.dataloader.entities.DataType;
import com.master.dataloader.repository.DataTypeRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataTypeService {

    private final DataTypeRepository dataTypeRepository;

    public DataTypeService(DataTypeRepository dataTypeRepository) {
        this.dataTypeRepository = dataTypeRepository;
    }

    public List<DataTypeDto> getAll() {
        return dataTypeRepository.findAll().stream().map(DataTypeDto::new).toList();
    }

    public List<DataTypeDto> getAllRemoteDtos() throws Exception {
        List<DataType> remoteDataTypes = getAllRemote();

        List<String> localDatasetIds = dataTypeRepository.findAllById(
                remoteDataTypes.stream().map(DataType::getId).toList()
        ).stream().map(DataType::getId).toList();

        List<DataTypeDto> result = remoteDataTypes.stream().map(DataTypeDto::new).toList();
        result.forEach(dt -> dt.setLoaded(localDatasetIds.contains(dt.getId())));
        return result;
    }

    public void deleteByIds(List<String> ids) {
        dataTypeRepository.deleteAllById(ids);
    }

    public void loadByIds(List<String> dataTypesIds) throws Exception {
        List<DataType> dataTypes = getAllRemote().stream()
                .filter(dataType -> dataTypesIds.contains(dataType.getId()))
                .toList();

        dataTypeRepository.saveAll(dataTypes);
    }

    private List<DataType> getAllRemote() throws Exception {
        Map<String, Object> requestParams = Utils.getBasicParams();

        String datasetsUrl = URLs.baseNoaaApiUrl + URLs.dataTypesUrl;

        return Utils.getRemoteData(datasetsUrl,requestParams, DataType.class);
    }

}
