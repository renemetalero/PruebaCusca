package com.shoppingcart.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderRegistrationRequest {
    private Long id;
    private String customer;
    private BigDecimal total;
    private String orderStatus;
    private String paymentMethod;
    private String paymentStatus;
    private List<OrderRegistrationDetailRequest> details;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomer() { return customer; }
    public void setCustomer(String customer) { this.customer = customer; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public List<OrderRegistrationDetailRequest> getDetails() { return details; }
    public void setDetails(List<OrderRegistrationDetailRequest> details) { this.details = details; }
}
