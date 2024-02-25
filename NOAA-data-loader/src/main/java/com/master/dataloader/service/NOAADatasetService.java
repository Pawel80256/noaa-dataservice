package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.dtos.NOAADatasetDto;
import com.master.dataloader.models.NOAADataset;
import com.master.dataloader.repository.NOAADatasetRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NOAADatasetService {

    private final NOAADatasetRepository noaaDatasetRepository;

    public NOAADatasetService(NOAADatasetRepository noaaDatasetRepository) {
        this.noaaDatasetRepository = noaaDatasetRepository;
    }

    public List<NOAADatasetDto> getAll() {
        return noaaDatasetRepository.findAll().stream().map(NOAADatasetDto::new).toList();
    }

    public List<NOAADatasetDto> getAllRemoteDtos() throws Exception {
        List<NOAADataset> remoteDatasets = getAllRemote();

        List<String> localDatasetsIds = noaaDatasetRepository.findAllById(
                remoteDatasets.stream().map(NOAADataset::getId).toList()
        ).stream().map(NOAADataset::getId).toList();

        List<NOAADatasetDto> result = remoteDatasets.stream().map(NOAADatasetDto::new).toList();
        result.forEach(ds -> ds.setLoaded(localDatasetsIds.contains(ds.getId())));
        return result;
    }

    public void loadByIds(List<String> datasetIds) throws Exception {
        List<NOAADataset> datasets = getAllRemote().stream()
                .filter(dataset -> datasetIds.contains(dataset.getId()))
                .toList();

        noaaDatasetRepository.saveAll(datasets);
    }

    public void deleteByIds(List<String> ids) {
        noaaDatasetRepository.deleteAllById(ids);
    }


    private List<NOAADataset> getAllRemote() throws Exception {
        Map<String, Object> requestParams = Utils.getBasicParams();

        String datasetsUrl = Constants.baseNoaaApiUrl + Constants.datasetsUrl;

        return Utils.getRemoteData(datasetsUrl,requestParams,NOAADataset.class);
    }
}
