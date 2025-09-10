package com.hudfs.hudfs28.dtos;

public class ProductRequest {
    private String name;
    private String category;
    private Float price;
    private Float calories;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }

    public Float getCalories() { return calories; }
    public void setCalories(Float calories) { this.calories = calories; }
}
