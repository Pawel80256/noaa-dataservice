package com.master.dataloader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElementService {
    private final ElementRepository elementRepository;

    public List<Element> getAll(){
        return elementRepository.findAll();
    }
}
