package com.hudfs.hudfs28.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;


@Entity
@Table(name = "creditCards")
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditCardId;
    
    private String cardNumber;
    private String cardExpireDate;
    private String cardCVC;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;
    
    // Constructors
    public CreditCard() {}
    
    public CreditCard(String cardNumber, String cardExpireDate, String cardCVC) {
        this.cardNumber = cardNumber;
        this.cardExpireDate = cardExpireDate;
        this.cardCVC = cardCVC;
    }
    
    // Getters and Setters
    public Long getCardId() { return creditCardId; }
    public void setCardId(Long creditCardId) { this.creditCardId = creditCardId; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    
    public String getCardExpireDate() { return cardExpireDate; }
    public void setCardExpireDate(String cardExpireDate) { this.cardExpireDate = cardExpireDate; }
    
    public String getCardCVC() { return cardCVC; }
    public void setCardCVC(String cardCVC) { this.cardCVC = cardCVC; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }    
}