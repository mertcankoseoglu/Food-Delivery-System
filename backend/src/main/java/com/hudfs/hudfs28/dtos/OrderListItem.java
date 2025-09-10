package com.hudfs.hudfs28.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class OrderListItem {
    private Long orderId;
    private String status;
    private LocalDateTime createdAt;
    private int itemCount;
    private AddressView address;
    private Long restaurantId;
    private String restaurantName;
    private Integer courierId;
    private String courierName;
    private String courierPhone;
    private boolean isCancelable;
    private List<String> itemNames;
    private Long creditCardId;
    private Long customerId;
    private Float totalPrice;
    private Float finalPrice;
    

// Add to constructor
public OrderListItem(Long orderId, String status, LocalDateTime createdAt,
                    int itemCount, AddressView address, Long restaurantId, String restaurantName,
                    String courierName, String courierPhone, Integer courierId, boolean isCancelable,
                    List<String> itemNames, Long creditCardId, Long customerId,
                    Float totalPrice, Float finalPrice) {
    this.orderId = orderId;
    this.status = status;
    this.createdAt = createdAt;
    this.itemCount = itemCount;
    this.address = address;
    this.restaurantId = restaurantId;
    this.restaurantName = restaurantName;
    this.courierName = courierName;
    this.courierPhone = courierPhone;
    this.courierId = courierId;
    this.isCancelable = isCancelable;
    this.itemNames = itemNames;
    this.creditCardId = creditCardId;
    this.customerId = customerId;
    this.totalPrice = totalPrice;
    this.finalPrice = finalPrice;
}


    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getItemCount() {
        return itemCount;
    }
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public AddressView getAddress() {
        return address;
    }
    public void setAddress(AddressView address) {
        this.address = address;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }
    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
    
    public String getRestaurantName() {
        return restaurantName;
    }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCourierName() {
        return courierName;
    }
    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
    }

    public Integer getCourierId() {
        return courierId;
    }
    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public boolean isCancelable() {
        return isCancelable;
    }
    public void setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }

    public List<String> getItemNames() {
        return itemNames;
    }
    public void setItemNames(List<String> itemNames) {
        this.itemNames = itemNames;
    }

    public Long getCreditCardId() {
        return creditCardId;
    }
    public void setCreditCardId(Long creditCardId) {
        this.creditCardId = creditCardId;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Float getFinalPrice() {
        return finalPrice;
    }
    public void setFinalPrice(Float finalPrice) {
        this.finalPrice = finalPrice;
    }
}
