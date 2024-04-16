package com.master.dataloader.service;

import com.master.dataloader.constant.URLs;
import com.master.dataloader.dtos.DataCategoryDto;
import com.master.dataloader.entities.DataCategory;
import com.master.dataloader.repository.DataCategoryRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataCategoryService {
    private final DataCategoryRepository dataCategoryRepository;

    public DataCategoryService(DataCategoryRepository dataCategoryRepository) {
        this.dataCategoryRepository = dataCategoryRepository;
    }

    public List<DataCategoryDto> getAll(){
        return dataCategoryRepository.findAll().stream().map(DataCategoryDto::new).toList();
    }

    public List<DataCategoryDto> getAllRemoteDtos() throws Exception {
        List<DataCategory> remoteDataCategories = getAllRemote();

        List<DataCategoryDto> result = remoteDataCategories.stream().map(DataCategoryDto::new).toList();

        List<String> localDataCategoriesIds = dataCategoryRepository.findAllById(
                remoteDataCategories.stream().map(DataCategory::getId).toList()
        ).stream().map(DataCategory::getId).toList();

        result.forEach(dc -> dc.setLoaded(localDataCategoriesIds.contains(dc.getId())));

        return result;
    }

    public void loadByIds(List<String> dataCategoriesIds) throws Exception {
        List<DataCategory> dataCategoriesToLoad = getAllRemote().stream()
                .filter(dc -> dataCategoriesIds.contains(dc.getId()))
                .toList();

        dataCategoryRepository.saveAll(dataCategoriesToLoad);
    }

    public void deleteByIds(List<String> ids){
        dataCategoryRepository.deleteAllById(ids);
    }

    private List<DataCategory> getAllRemote() throws Exception {
        Map<String, Object> requestParams = Utils.getBasicParams();

        String datasetsUrl = URLs.baseNoaaApiUrl + URLs.dataCategoriesUrl;

        return Utils.getRemoteData(datasetsUrl,requestParams, DataCategory.class);
    }
}
