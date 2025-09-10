package com.hudfs.hudfs28.dtos;

public class RestaurantInfo {
    private Long restaurantId;
    private String name;
    private boolean isOpen;

    public RestaurantInfo() {}

    public RestaurantInfo(Long restaurantId, String name, boolean isOpen) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.isOpen = isOpen;
    }

    // Getters and Setters
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean isOpen) { this.isOpen = isOpen; }
}
