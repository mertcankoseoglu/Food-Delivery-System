package com.hudfs.hudfs28.dtos;

public class CourierProfileUpdateResponse {
    private boolean status;
    private String detail;

    public CourierProfileUpdateResponse() {}
    public CourierProfileUpdateResponse(boolean status, String detail) {
        this.status = status;
        this.detail = detail;
    }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}