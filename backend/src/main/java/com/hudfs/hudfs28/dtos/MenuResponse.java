package com.hudfs.hudfs28.dtos;

public class MenuResponse {
    private boolean success;
    private String detail;

    public MenuResponse() {}

    public MenuResponse(boolean success, String detail) {
        this.success = success;
        this.detail = detail;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}
