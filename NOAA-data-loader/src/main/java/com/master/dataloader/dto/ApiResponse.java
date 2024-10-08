package com.master.dataloader.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class ApiResponse {
    private final Integer responseCode;
    private final String responseData;

    public ApiResponse(Integer responseCode, String responseData) {
        this.responseCode = responseCode;
        this.responseData = responseData;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getResponseData() {
        return responseData;
    }
}
