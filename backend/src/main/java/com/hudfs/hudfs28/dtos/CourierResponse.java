package com.hudfs.hudfs28.dtos;

public class CourierResponse {
    private boolean status;
    private String token;
    private Integer courierId;
    private String detail;

    public CourierResponse() {}

    public CourierResponse(boolean status, String token, Integer courierId, String detail) {
        this.status = status;
        this.token = token;
        this.courierId = courierId;
        this.detail = detail;
    }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Integer getCourierId() { return courierId; }
    public void setCourierId(Integer courierId) { this.courierId = courierId; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}
