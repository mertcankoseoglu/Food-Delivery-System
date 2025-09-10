package com.hudfs.hudfs28.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    private String name;
    private String category;
    private Float price;
    private Float calories;

    @ManyToMany
    private List<Product> products;

    @Column(nullable = false)
    private boolean inStock = true;

    @ManyToOne
    private Restaurant restaurant;

    public Menu() {}

    public Menu(String name, String category, Float price, Float calories, List<Product> products, Restaurant restaurant) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.calories = calories;
        this.products = products;
        this.restaurant = restaurant;
    }

    // Getter ve Setter'lar
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

    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isInStock() {
        return inStock;
    }
    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
}