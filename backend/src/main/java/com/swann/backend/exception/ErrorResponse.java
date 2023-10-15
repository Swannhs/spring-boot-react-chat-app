package com.swann.backend.exception;

public class ErrorResponse {

    private String error;
    private String message;

    // Constructor
    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    // Getters and setters for 'error' and 'message' fields
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
