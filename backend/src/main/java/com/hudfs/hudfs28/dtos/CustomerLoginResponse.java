package com.hudfs.hudfs28.dtos;

public class CustomerLoginResponse {
    private boolean status;
    private String token;
    private Long customerId;
    private String detail;

    public CustomerLoginResponse(boolean status, String token, Long customerId, String detail) {
        this.status = status;
        this.token = token;
        this.customerId = customerId;
        this.detail = detail;
    }

    // Getters
    public boolean isStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getDetail() {
        return detail;
    }

    // Setters
    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
