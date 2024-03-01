package com.master.dataloader.controller;

import com.master.dataloader.dtos.NOAADataTypeDto;
import com.master.dataloader.service.NOAADataTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/NOAA/datatypes")
public class NOAADataTypeController {

    private final NOAADataTypeService noaaDataTypeService;

    public NOAADataTypeController(NOAADataTypeService noaaDataTypeService) {
        this.noaaDataTypeService = noaaDataTypeService;
    }

    @GetMapping
    public ResponseEntity<List<NOAADataTypeDto>> getAll(){
        return ResponseEntity.ok(noaaDataTypeService.getAll());
    }

    @GetMapping("/remote")
    public ResponseEntity<List<NOAADataTypeDto>> getAllRemote() throws Exception {
        return ResponseEntity.ok(noaaDataTypeService.getAllRemoteDtos());
    }


    @PutMapping("loadByIds")
    public ResponseEntity<Void> loadByIds(@RequestBody List<String> ids) throws Exception {
        noaaDataTypeService.loadByIds(ids);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> ids){
        noaaDataTypeService.deleteByIds(ids);
        return ResponseEntity.ok().build();
    }
}
