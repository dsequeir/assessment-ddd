package com.example.assessmentddd.infrastructure.web;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API error response")
public class ApiErrorResponse {

    @Schema(example = "404")
    private int status;

    @Schema(example = "PRICE_NOT_FOUND")
    private String error;

    @Schema(example = "No applicable price found for the given criteria")
    private String message;

    @Schema(example = "/api/v1/prices")
    private String path;

    @Schema(example = "2026-03-26T10:15:30")
    private String timestamp;

    public ApiErrorResponse(int status, String error, String message, String path, String timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}