package com.hudfs.hudfs28.dtos;

import java.util.List;

public class MenuRequest {
    private String name;
    private String category;
    private Float price;
    private List<ProductShortInfo> products;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }

    public List<ProductShortInfo> getProducts() { return products; }
    public void setProducts(List<ProductShortInfo> products) { this.products = products; }
}
