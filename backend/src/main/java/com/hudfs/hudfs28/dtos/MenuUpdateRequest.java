package com.hudfs.hudfs28.dtos;

public class MenuUpdateRequest {
    private String name;
    private String category;
    private Float price;

    public MenuUpdateRequest() {
    }

    public MenuUpdateRequest(String name, String category, Float price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // Getter Setter
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Float getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

}
