package com.hudfs.hudfs28.dtos;

public class CartAddRequest {
    private String type;     // "product" or "menu"
    private Long targetId;   // productId or menuId

    public CartAddRequest() {}

    public CartAddRequest(String type, Long targetId) {
        this.type = type;
        this.targetId = targetId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
