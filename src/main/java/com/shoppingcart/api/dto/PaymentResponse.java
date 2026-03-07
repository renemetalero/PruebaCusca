package com.shoppingcart.api.dto;

import java.math.BigDecimal;

public class PaymentResponse {

    private Long paymentId;
    private Long orderId;
    private String status;
    private BigDecimal amount;

    public PaymentResponse() {
    }

    public PaymentResponse(Long paymentId, Long orderId, String status, BigDecimal amount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
