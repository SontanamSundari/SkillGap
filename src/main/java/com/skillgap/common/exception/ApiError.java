package com.skillgap.common.exception;

import java.time.Instant;

public class ApiError {

    private int status;
    private String message;
    private Instant timestamp;

    public ApiError() {
    }

    public ApiError(int status, String message, Instant timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}