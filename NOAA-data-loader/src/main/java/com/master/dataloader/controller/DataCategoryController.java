package com.master.dataloader.controller;

import com.master.dataloader.dtos.DataCategoryDto;
import com.master.dataloader.service.DataCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("NOAA/datacategories")
public class DataCategoryController {

    private final DataCategoryService dataCategoryService;

    public DataCategoryController(DataCategoryService dataCategoryService) {
        this.dataCategoryService = dataCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<DataCategoryDto>> getAll(){
        return ResponseEntity.ok(dataCategoryService.getAll());
    }

    @GetMapping("/remote")
    public ResponseEntity<List<DataCategoryDto>> getAllRemote() throws Exception {
        return ResponseEntity.ok(dataCategoryService.getAllRemoteDtos());
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadByIds(@RequestBody List<String> dataCategoriesIds) throws Exception {
        dataCategoryService.loadByIds(dataCategoriesIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> dataCategoriesIds){
        dataCategoryService.deleteByIds(dataCategoriesIds);
        return ResponseEntity.ok().build();
    }
}
