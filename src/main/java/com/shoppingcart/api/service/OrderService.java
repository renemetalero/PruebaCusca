package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.OrderRegistrationRequest;
import com.shoppingcart.api.dto.OrderRegistrationResponse;
import com.shoppingcart.api.entity.OrderEntity;

import java.util.List;

public interface OrderService {
    OrderRegistrationResponse createOrder(OrderRegistrationRequest request);
    OrderRegistrationResponse updateOrder(OrderRegistrationRequest request);
    void disableOrder(Long id);
    List<OrderRegistrationResponse> getAllOrders();
    OrderRegistrationResponse getOrder(Long id);
    OrderEntity getOrderEntity(Long id);
    void save(OrderEntity order);
}
