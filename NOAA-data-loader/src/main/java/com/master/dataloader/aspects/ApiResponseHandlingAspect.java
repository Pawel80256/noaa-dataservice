package com.master.dataloader.aspects;

import com.master.dataloader.dto.ApiResponse;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;

@Aspect
public class ApiResponseHandlingAspect {
    private final Logger log = LoggerFactory.getLogger(ApiResponseHandlingAspect.class);

    @Pointcut("execution(private * com.master.dataloader.utils.Utils.sendRequest(..))")
    public void sendRequestMethod(){}

    @AfterReturning(pointcut = "sendRequestMethod()", returning = "apiResponse")
    public void afterReturningApiResponse(ApiResponse apiResponse){
        Integer responseCode = apiResponse.getResponseCode();
        log.info(String.format("Remote api call ended with status: %d", responseCode));

        if (!(responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_BAD_REQUEST)){
            log.info("bad request");
        }
    }
}
