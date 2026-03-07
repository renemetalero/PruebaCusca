package com.shoppingcart.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class PaymentRequest {

    @JsonAlias("idOrder")
    private Long orderId;

    @JsonAlias("number_card")
    private String cardNumber;

    @JsonAlias("names")
    private String cardHolder;

    public PaymentRequest() {
    }

    public PaymentRequest(Long orderId, String cardNumber, String cardHolder) {
        this.orderId = orderId;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
}
