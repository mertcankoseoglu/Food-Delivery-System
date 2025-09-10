package com.hudfs.hudfs28.dtos;


public class RestaurantProfileResponse {
    private boolean success;
    private String detail;

    public RestaurantProfileResponse(boolean success, String detail) {
        this.success = success;
        this.detail = detail;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}
