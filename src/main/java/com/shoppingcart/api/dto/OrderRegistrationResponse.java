package com.shoppingcart.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderRegistrationResponse {
    private Long id;
    private String customer;
    private BigDecimal total;
    private String orderStatus;
    private String paymentMethod;
    private String paymentStatus;
    private Boolean enabled;
    private List<OrderRegistrationDetailRequest> details;

    public OrderRegistrationResponse(Long id, String customer, BigDecimal total, String orderStatus,
                                     String paymentMethod, String paymentStatus, Boolean enabled,
                                     List<OrderRegistrationDetailRequest> details) {
        this.id = id;
        this.customer = customer;
        this.total = total;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.enabled = enabled;
        this.details = details;
    }

    public Long getId() { return id; }
    public String getCustomer() { return customer; }
    public BigDecimal getTotal() { return total; }
    public String getOrderStatus() { return orderStatus; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public Boolean getEnabled() { return enabled; }
    public List<OrderRegistrationDetailRequest> getDetails() { return details; }
}
