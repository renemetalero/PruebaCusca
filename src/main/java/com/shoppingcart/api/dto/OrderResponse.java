package com.shoppingcart.api.dto;

import com.shoppingcart.api.entity.OrderStatus;

import java.time.LocalDateTime;

public class OrderResponse {

    private Long id;
    private Long clientId;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long clientId, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.clientId = clientId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
