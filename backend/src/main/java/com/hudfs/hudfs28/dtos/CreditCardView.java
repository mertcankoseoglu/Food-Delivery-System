package com.hudfs.hudfs28.dtos;

public class CreditCardView {
    private Long creditCardId;
    private String cardNumber;
    private String cardExpireDate;
    private String cardCVC;

    // Getters and setters

    public Long getCreditCardId() { return creditCardId; }
    public void setCreditCardId(Long creditCardId) { this.creditCardId = creditCardId; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCardExpireDate() { return cardExpireDate; }
    public void setCardExpireDate(String cardExpireDate) { this.cardExpireDate = cardExpireDate; }

    public String getCardCVC() { return cardCVC; }
    public void setCardCVC(String cardCVC) { this.cardCVC = cardCVC; }
}
