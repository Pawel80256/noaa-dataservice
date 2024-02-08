package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataset;
import com.master.dataloader.repository.NOAADatasetRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NOAADatasetService {

    private final NOAADatasetRepository noaaDatasetRepository;

    public NOAADatasetService(NOAADatasetRepository noaaDatasetRepository) {
        this.noaaDatasetRepository = noaaDatasetRepository;
    }

    public List<NOAADataset> getAll(){
        return noaaDatasetRepository.findAll();
    }

    public PaginationWrapper<NOAADataset> getAllRemote(Integer limit, Integer offset) throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",limit);
        requestParams.put("offset",offset);

        String datasetsUrl = Constants.baseNoaaApiUrl + Constants.datasetsUrl;
        String requestResult = Utils.sendRequest(datasetsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        JsonNode paginationDataNode = rootNode.path("metadata").path("resultset");
        JsonNode resultsNode = rootNode.path("results");

        PaginationData paginationData = mapper.readerFor(PaginationData.class).readValue(paginationDataNode);
        List<NOAADataset> result = mapper.readerForListOf(NOAADataset.class).readValue(resultsNode);

        return new PaginationWrapper<>(paginationData.getOffset(),paginationData.getCount(),paginationData.getLimit(),result);
    }

    public void loadAll() throws Exception {
        List<NOAADataset> datasets = getAllRemote(11,1).getData();
        noaaDatasetRepository.saveAll(datasets);
    }
}
