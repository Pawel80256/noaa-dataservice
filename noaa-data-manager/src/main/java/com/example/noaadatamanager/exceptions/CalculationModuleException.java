package com.example.noaadatamanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CalculationModuleException extends RuntimeException{
    public CalculationModuleException(String message){super(message);}
}
