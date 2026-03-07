package com.shoppingcart.api.dto;

public class PaymentRegistrationRequest {
    private Long id;
    private Long idOrder;
    private String names;
    private String surnames;
    private String email;
    private String phone;
    private String number_card;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdOrder() { return idOrder; }
    public void setIdOrder(Long idOrder) { this.idOrder = idOrder; }
    public String getNames() { return names; }
    public void setNames(String names) { this.names = names; }
    public String getSurnames() { return surnames; }
    public void setSurnames(String surnames) { this.surnames = surnames; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getNumber_card() { return number_card; }
    public void setNumber_card(String number_card) { this.number_card = number_card; }
}
