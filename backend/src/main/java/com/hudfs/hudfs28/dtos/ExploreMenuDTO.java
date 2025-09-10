package com.hudfs.hudfs28.dtos;

import java.util.List;

public class ExploreMenuDTO {
    private Long menuId;
    private String name;
    private String category;
    private Float price;
    private Float calories;
    private int likeCount;
    private int dislikeCount;
    private boolean inStock;
    private List<ProductShortInfo> products;
    private RestaurantInfo restaurant;

    public ExploreMenuDTO(Long menuId, String name, String category, Float price, Float calories, boolean inStock,
                          int likeCount, int dislikeCount, List<ProductShortInfo> products, RestaurantInfo restaurant) {
        this.menuId = menuId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.calories = calories;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.products = products;
        this.restaurant = restaurant;
        this.inStock = inStock;
    }

    // Getters and setters
    public Long getMenuId() {
        return menuId;
    }
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getCalories() {
        return calories;
    }
    public void setCalories(Float calories) {
        this.calories = calories;
    }

    public int getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }
    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public List<ProductShortInfo> getProducts() {
        return products;
    }
    public void setProducts(List<ProductShortInfo> products) {
        this.products = products;
    }

    public RestaurantInfo getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(RestaurantInfo restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isInStock() {
        return inStock;
    }
    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
}