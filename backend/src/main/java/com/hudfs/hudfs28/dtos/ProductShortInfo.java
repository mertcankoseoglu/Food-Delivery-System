package com.hudfs.hudfs28.dtos;

public class ProductShortInfo {
    private Long productId;
    private String name;

    public ProductShortInfo() {}
    
    public ProductShortInfo(Long productId, String name) {
        this.productId = productId;
        this.name = name;
    }

    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
