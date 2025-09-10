package com.hudfs.hudfs28.dtos;

public class CustomerResponse {
    private boolean status;
    private String token;
    private Long customerId;
    private String detail;

    public CustomerResponse() {}

    public CustomerResponse(boolean status, String token, Long customerId, String detail) {
        this.status = status;
        this.token = token;
        this.customerId = customerId;
        this.detail = detail;
    }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}
