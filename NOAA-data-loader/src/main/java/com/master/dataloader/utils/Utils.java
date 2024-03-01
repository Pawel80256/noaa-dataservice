package com.master.dataloader.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.models.NOAAData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {
    public static String buildUrlWithParams(String baseUrl, Map<String, Object> params) throws Exception {
        if(params == null){
            params = new HashMap<>();
        }
        StringJoiner urlWithParams = new StringJoiner("&", baseUrl + "?", "");
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (param.getValue() != null && !param.getValue().toString().isEmpty()) {
                urlWithParams.add(URLEncoder.encode(param.getKey(), "UTF-8") + "=" + URLEncoder.encode(param.getValue().toString(), "UTF-8"));
            }
        }
        return urlWithParams.toString();
    }

    public static void addTokenHeader(HttpURLConnection connection){
        //move to file
        connection.setRequestProperty("token","kSwPNBVuPrfrjOIXXGllzYQSrVWCfTec");
    }

    public static <T> List<T> getRemoteData(String url, Map<String, Object> params, Class<T> tClass) throws Exception {
        List<T> result = new ArrayList<>();
        String requestResult;
        JsonNode rootNode, resultsNode;

        requestResult = sendRequest(url, params);

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        rootNode = mapper.readTree(requestResult);

        PaginationData paginationData = mapper.readerFor(PaginationData.class)
                .readValue(rootNode.path("metadata").path("resultset"));
        resultsNode = rootNode.path("results");

        result.addAll(
                mapper.readerForListOf(tClass).readValue(resultsNode)
        );

        if(paginationData.getCount() > 100){
            for (int i=1001; i<paginationData.getCount() ; i+=1000){
                params.put("offset",i);
                String additionalRequestResult = Utils.sendRequest(url,params);
                JsonNode additionalResultsNode = mapper.readTree(additionalRequestResult).path("results");
                result.addAll(
                        mapper.readerForListOf(tClass).readValue(additionalResultsNode)
                );
            }
        }

        return result;
    }

    public static Map<String, Object> getBasicParams(){
        Map<String,Object> result = new HashMap<>();
        result.put("limit", 1000);
        result.put("offset",1);
        return result;
    }

    private static String sendRequest(String urlString, Map<String,Object> params) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(
                Utils.buildUrlWithParams(urlString,params)
        );

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        Utils.addTokenHeader(connection);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }


}
