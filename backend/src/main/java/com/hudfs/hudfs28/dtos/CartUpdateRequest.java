package com.hudfs.hudfs28.dtos;

public class CartUpdateRequest {
    private int number;

    public CartUpdateRequest() {}

    public CartUpdateRequest(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
