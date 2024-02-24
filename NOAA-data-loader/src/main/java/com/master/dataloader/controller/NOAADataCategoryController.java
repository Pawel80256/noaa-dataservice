package com.master.dataloader.controller;

import com.master.dataloader.dtos.NOAADataCategoryDto;
import com.master.dataloader.service.NOAADataCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("NOAA/datacategories")
public class NOAADataCategoryController {

    private final NOAADataCategoryService noaaDataCategoryService;

    public NOAADataCategoryController(NOAADataCategoryService noaaDataCategoryService) {
        this.noaaDataCategoryService = noaaDataCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<NOAADataCategoryDto>> getAll(){
        return ResponseEntity.ok(noaaDataCategoryService.getAll());
    }

    @GetMapping("/remote")
    public ResponseEntity<List<NOAADataCategoryDto>> getAllRemote() throws Exception {
        return ResponseEntity.ok(noaaDataCategoryService.getAllRemoteDtos());
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadByIds(@RequestBody List<String> dataCategoriesIds) throws Exception {
        noaaDataCategoryService.loadByIds(dataCategoriesIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> dataCategoriesIds){
        noaaDataCategoryService.deleteByIds(dataCategoriesIds);
        return ResponseEntity.ok().build();
    }
}
