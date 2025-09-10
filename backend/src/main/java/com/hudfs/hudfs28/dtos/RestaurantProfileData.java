package com.hudfs.hudfs28.dtos;

// import com.fasterxml.jackson.annotation.JsonInclude;

public class RestaurantProfileData {
    private Long id;
    private String name;
    private String owner;
    private String mail;
    private String phoneNumber;
    private Float overAllRating;
    private boolean isOpen;

    // @JsonInclude(JsonInclude.Include.NON_NULL)
    private AddressView address;

    public RestaurantProfileData(Long id, String name, String owner, String mail, String phoneNumber, Float overAllRating, AddressView address, boolean isOpen) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.overAllRating = overAllRating;
        this.address = address;
        this.isOpen = isOpen;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getOwner() { return owner; }
    public String getMail() { return mail; }
    public String getPhoneNumber() { return phoneNumber; }
    public Float getOverAllRating() { return overAllRating; }
    public AddressView getAddress() { return address; }
    public boolean isOpen() { return isOpen; }
}