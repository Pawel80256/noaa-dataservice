package com.master.dataloader.controller;

import com.master.dataloader.service.ElementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/element")
public class ElementController {
    private final ElementService elementService;

    @GetMapping
    public List<Element> getAll(){
        return elementService.getAll();
    }
}
