package com.hudfs.hudfs28.dtos;

import java.util.List;

public class CustomerMenuResponse {
    private Long menuId;
    private String name;
    private String category;
    private Float price;
    private Float calories;
    private List<ProductShortInfo> products;
    private RestaurantInfo restaurant;
    private boolean inStock;

    public CustomerMenuResponse() {}

    public CustomerMenuResponse(Long menuId, String name, String category, Float price, Float calories, boolean inStock, List<ProductShortInfo> products, RestaurantInfo restaurant) {
        this.menuId = menuId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.calories = calories;
        this.products = products;
        this.restaurant = restaurant;
        this.inStock = inStock;
    }

    // Getters and Setters
    public Long getMenuId() { return menuId; }
    public void setMenuId(Long menuId) { this.menuId = menuId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }

    public Float getCalories() { return calories; }
    public void setCalories(Float calories) { this.calories = calories; }

    public List<ProductShortInfo> getProducts() { return products; }
    public void setProducts(List<ProductShortInfo> products) { this.products = products; }

    public RestaurantInfo getRestaurant() { return restaurant; }
    public void setRestaurant(RestaurantInfo restaurant) { this.restaurant = restaurant; }

    public boolean isInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }
}