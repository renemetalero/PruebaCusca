package com.shoppingcart.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class OrderDetailRequest {

    @JsonAlias("idOrder")
    private Long orderId;

    @JsonAlias("idProduct")
    private Long productId;

    private Integer quantity;

    public OrderDetailRequest() {
    }

    public OrderDetailRequest(Long orderId, Long productId, Integer quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
