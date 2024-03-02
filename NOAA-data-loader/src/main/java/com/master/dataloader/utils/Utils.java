package com.master.dataloader.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.configuration.ApiProperties;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.models.NOAAData;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {
    public static String buildUrlWithParams(String baseUrl, Map<String, Object> params) throws UnsupportedEncodingException {
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
        connection.setRequestProperty("token", ApiProperties.getInstance().getToken());
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

    private static String sendRequest(String urlString, Map<String,Object> params) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(Utils.buildUrlWithParams(urlString, params));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Utils.addTokenHeader(connection);

        int responseCode = connection.getResponseCode(); //todo: maybe create afterReturning aspect?
        InputStream inputStream;

        if (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }

        return result.toString();
    }



}
