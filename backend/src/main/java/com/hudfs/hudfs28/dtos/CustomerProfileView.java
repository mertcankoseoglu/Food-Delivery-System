package com.hudfs.hudfs28.dtos;

import java.util.List;

public class CustomerProfileView {
    private String name;
    private String mail;
    private Integer age;
    private String phoneNumber;
    private List<AddressView> addresses;
    private List<CreditCardView> creditCards;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<AddressView> getAddresses() { return addresses; }
    public void setAddresses(List<AddressView> addresses) { this.addresses = addresses; }

    public List<CreditCardView> getCreditCards() { return creditCards; }
    public void setCreditCards(List<CreditCardView> creditCards) { this.creditCards = creditCards; }
}
