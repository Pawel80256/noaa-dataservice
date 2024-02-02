package com.master.dataloader.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

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

    public static String sendRequest(String urlString, Map<String,Object> params) throws Exception {
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
