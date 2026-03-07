package com.shoppingcart.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class CreateOrderRequest {

    @JsonAlias("idClient")
    private Long clientId;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(Long clientId) {
        this.clientId = clientId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
