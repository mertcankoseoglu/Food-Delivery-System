package com.hudfs.hudfs28.dtos;

public class OrderPlacementRequest {
    private Long addressId;
    private Long creditCardId;
    private Integer couponId;

    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }

    public Long getCreditCardId() { return creditCardId; }
    public void setCreditCardId(Long creditCardId) { this.creditCardId = creditCardId; }

    public Integer getCouponId() { return couponId; }
    public void setCouponId(Integer couponId) { this.couponId= couponId; }
}
