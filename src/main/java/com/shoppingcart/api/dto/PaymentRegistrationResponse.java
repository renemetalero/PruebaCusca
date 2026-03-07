package com.shoppingcart.api.dto;

import java.math.BigDecimal;

public class PaymentRegistrationResponse {
    private Long id;
    private Long idOrder;
    private String paymentStatus;
    private String names;
    private String surnames;
    private String email;
    private String phone;
    private BigDecimal amount;
    private Boolean enabled;

    public PaymentRegistrationResponse(Long id, Long idOrder, String paymentStatus, String names, String surnames,
                                       String email, String phone, BigDecimal amount, Boolean enabled) {
        this.id = id;
        this.idOrder = idOrder;
        this.paymentStatus = paymentStatus;
        this.names = names;
        this.surnames = surnames;
        this.email = email;
        this.phone = phone;
        this.amount = amount;
        this.enabled = enabled;
    }

    public Long getId() { return id; }
    public Long getIdOrder() { return idOrder; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getNames() { return names; }
    public String getSurnames() { return surnames; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public BigDecimal getAmount() { return amount; }
    public Boolean getEnabled() { return enabled; }
}
