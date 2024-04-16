package com.master.dataloader.service;

import com.master.dataloader.constant.URLs;
import com.master.dataloader.dtos.DatasetDto;
import com.master.dataloader.entities.Dataset;
import com.master.dataloader.repository.DatasetRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatasetService {

    private final DatasetRepository datasetRepository;

    public DatasetService(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    public List<DatasetDto> getAll() {
        return datasetRepository.findAll().stream().map(DatasetDto::new).toList();
    }

    public List<DatasetDto> getAllRemoteDtos() throws Exception {
        List<Dataset> remoteDatasets = getAllRemote();

        List<String> localDatasetsIds = datasetRepository.findAllById(
                remoteDatasets.stream().map(Dataset::getId).toList()
        ).stream().map(Dataset::getId).toList();

        List<DatasetDto> result = remoteDatasets.stream().map(DatasetDto::new).toList();
        result.forEach(ds -> ds.setLoaded(localDatasetsIds.contains(ds.getId())));
        return result;
    }

    public void loadByIds(List<String> datasetIds) throws Exception {
        List<Dataset> datasets = getAllRemote().stream()
                .filter(dataset -> datasetIds.contains(dataset.getId()))
                .toList();

        datasetRepository.saveAll(datasets);
    }

    public void deleteByIds(List<String> ids) {
        datasetRepository.deleteAllById(ids);
    }


    private List<Dataset> getAllRemote() throws Exception {
        Map<String, Object> requestParams = Utils.getBasicParams();

        String datasetsUrl = URLs.baseNoaaApiUrl + URLs.datasetsUrl;

        return Utils.getRemoteData(datasetsUrl,requestParams, Dataset.class);
    }
}
