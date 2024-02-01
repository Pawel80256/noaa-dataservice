package com.master.dataloader.utils;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.StringJoiner;

public class Utils {
    public static String buildUrlWithParams(String baseUrl, Map<String, Object> params) throws Exception {
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

}
