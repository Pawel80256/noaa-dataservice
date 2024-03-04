package com.master.dataloader.exceptions;

import com.master.dataloader.aspects.UnsuccessfulRemoteApiCallException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceInUseException.class)
    public ResponseEntity<Object> handleResourceInUseException(ResourceInUseException ex, WebRequest rq) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Error while deleting data, resource is in use",
                ex.getMessage(),
                rq.getDescription(false) + " (" + ((ServletWebRequest) rq).getHttpMethod() + ")"        );

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(MissingRelatedResourceException.class)
    public ResponseEntity<Object> handleMissingRelatedResourceException(MissingRelatedResourceException ex, WebRequest rq) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Error while loading data, missing related resource",
                ex.getMessage(),
                rq.getDescription(false) + " (" + ((ServletWebRequest) rq).getHttpMethod() + ")"
        );

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UnsuccessfulRemoteApiCallException.class)
    public ResponseEntity<Object> handleUnsuccessfulRemoteApiCallException(UnsuccessfulRemoteApiCallException ex, WebRequest rq){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                ex.getReason(),
                rq.getDescription(false) + " (" + ((ServletWebRequest) rq).getHttpMethod() + ")"
        );

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
