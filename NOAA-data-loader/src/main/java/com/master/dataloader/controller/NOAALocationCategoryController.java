package com.master.dataloader.controller;

import com.master.dataloader.dtos.NoaaLocationCategoryDto;
import com.master.dataloader.models.NOAALocationCategory;
import com.master.dataloader.service.NOAALocationCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("NOAA/locationcategory")
public class NOAALocationCategoryController {

    private final NOAALocationCategoryService noaaLocationCategoryService;

    public NOAALocationCategoryController(NOAALocationCategoryService noaaLocationCategoryService) {
        this.noaaLocationCategoryService = noaaLocationCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<NoaaLocationCategoryDto>> getAll(){
        return ResponseEntity.ok(noaaLocationCategoryService.getAll());
    }

    @GetMapping("/remote")
    public ResponseEntity<List<NoaaLocationCategoryDto>> getAllRemote() throws Exception {
        return ResponseEntity.ok(
                noaaLocationCategoryService.getAllRemoteDtos()
        );
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadAll() throws Exception {
        noaaLocationCategoryService.loadAll();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/loadByIds")
    public ResponseEntity<Void> loadByIds(@RequestBody List<String> ids) throws Exception {
        noaaLocationCategoryService.loadByIds(ids);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> ids) {
        noaaLocationCategoryService.deleteByIds(ids);
        return ResponseEntity.ok().build();
    }

}
