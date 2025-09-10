package com.hudfs.hudfs28.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    
    private String name;
    private String mail;
    private String password;
    
    private Integer age;
    private String phoneNumber;
    private Integer level;
    private Integer points;

    @OneToOne
    private Avatar avatar;
    
    // Customer -> Address: One-to-Many
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    // Customer -> CreditCard: One-to-Many
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditCard> creditCards = new ArrayList<>();
    
    public Customer() {}
    
    public Customer(String name, String mail, String password) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.age = 0;
        this.phoneNumber = "";
        this.level = 0;
        this.points = 0;
    }
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    
    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }

    public List<CreditCard> getCreditCards() { return creditCards; }
    public void setCreditCards(List<CreditCard> creditCards) { this.creditCards = creditCards; }

    public Avatar getAvatar() {return avatar;}
    public void setAvatar(Avatar avatar) {this.avatar = avatar;}
}    
