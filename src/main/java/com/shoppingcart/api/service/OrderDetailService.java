package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.OrderDetailRequest;
import com.shoppingcart.api.dto.OrderDetailResponse;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDetailService {
    OrderDetailResponse addDetail(Long orderId, OrderDetailRequest request);
    List<OrderDetailResponse> getDetailsByOrder(Long orderId);
    BigDecimal calculateOrderTotal(Long orderId);
}
