package com.hudfs.hudfs28.dtos;

public class RestaurantResponse {
    private boolean status;
    private String token;
    private Long restaurantId;
    private String detail;

    public RestaurantResponse() {}

    public RestaurantResponse(boolean status, String token, Long restaurantId, String detail) {
        this.status = status;
        this.token = token;
        this.restaurantId = restaurantId;
        this.detail = detail;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
