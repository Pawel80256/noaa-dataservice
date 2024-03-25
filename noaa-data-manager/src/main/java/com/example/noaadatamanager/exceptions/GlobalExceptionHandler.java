package com.example.noaadatamanager.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedAccessException ex, WebRequest rq) {
        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized access",
                ex.getMessage(),
                rq.getDescription(false) + " (" + ((ServletWebRequest) rq).getHttpMethod() + ")"        );

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest rq) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Data validation error",
                ex.getMessage(),
                rq.getDescription(false) + " (" + ((ServletWebRequest) rq).getHttpMethod() + ")"        );

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    //todo: move to aspect
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex, WebRequest rq){
        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED,
                "Authorization token is expired",
                ex.getMessage(),
                rq.getDescription(false) + " (" + ((ServletWebRequest) rq).getHttpMethod() + ")"        );

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    public static class ApiError {
        private HttpStatus status;
        private String message;
        private String details;
        private String debugMessage;

        public ApiError(HttpStatus status, String message, String details, String debugMessage) {
            this.status = status;
            this.message = message;
            this.details = details;
            this.debugMessage = debugMessage;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public String getDetails() {
            return details;
        }

        public String getDebugMessage() {
            return debugMessage;
        }

        public void setStatus(HttpStatus status) {
            this.status = status;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public void setDebugMessage(String debugMessage) {
            this.debugMessage = debugMessage;
        }
    }
}
