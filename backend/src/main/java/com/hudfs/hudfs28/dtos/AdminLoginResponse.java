package com.hudfs.hudfs28.dtos;

public class AdminLoginResponse {
    private boolean status;
    private String token;
    private String detail;

    public AdminLoginResponse(boolean status, String token, String detail) {
        this.status = status;
        this.token = token;
        this.detail = detail;
    }

    // Getters and Setters
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}
