package com.hudfs.hudfs28.dtos;

public class OrderStatusUpdateRequest {
    private String newStatus; // "PREPARING", "READY", etc.

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
}
