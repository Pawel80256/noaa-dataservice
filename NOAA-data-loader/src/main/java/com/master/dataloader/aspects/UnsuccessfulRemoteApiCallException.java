package com.master.dataloader.aspects;

public class UnsuccessfulRemoteApiCallException extends RuntimeException{
    private final String reason;

    public UnsuccessfulRemoteApiCallException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
