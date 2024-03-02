package com.master.dataloader.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceInUseException.class)
    public ResponseEntity<Object> handleResourceInUseException(ResourceInUseException ex, WebRequest rq){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Error encountered",
                ex.getMessage(),
                rq.getDescription(false)
        );

        return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
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
