package com.example.noaadatamanager.controller;

import com.example.noaadatamanager.annotations.RequestAuthorization;
import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.dtos.update.MeasurementUpdateCommentDto;
import com.example.noaadatamanager.dtos.update.MeasurementUpdateValueDto;
import com.example.noaadatamanager.service.MeasurementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    private final RestClient restClient;

    public MeasurementController(MeasurementService measurementService, RestClient restClient) {
        this.measurementService = measurementService;
        this.restClient = restClient;
    }

    @PostMapping
    @RequestAuthorization(roles = {"ADMIN","MEASUREMENT_MANAGER"})
    public ResponseEntity<String> create(@RequestBody MeasurementInputDto measurementInputDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(measurementService.create(measurementInputDto));
    }

    @DeleteMapping
    @RequestAuthorization(roles = {"ADMIN","MEASUREMENT_MANAGER"})
    public ResponseEntity<Void> delete(@RequestParam(name = "measurementId") String measurementId){
        measurementService.delete(measurementId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/value")
    @RequestAuthorization(roles = {"ADMIN","MEASUREMENT_MANAGER"})
    public ResponseEntity<Void> updateValue(@RequestBody MeasurementUpdateValueDto dto){
        measurementService.updateValue(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/comment")
    @RequestAuthorization(roles = {"ADMIN","MEASUREMENT_MANAGER"})
    public ResponseEntity<Void> updateComment(@RequestBody MeasurementUpdateCommentDto dto){
        measurementService.updateComment(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/calcModuleTest")
    public ResponseEntity<String> test(){
        String result = restClient
                .get()
                .uri("/measurements")
                .retrieve()
                .body(String.class);

        return ResponseEntity.ok(result);
    }
}
