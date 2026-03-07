package com.shoppingcart.api.dto;

import java.util.List;

public class FullOrderResponse {

    private OrderResponse order;
    private List<OrderDetailResponse> details;

    public FullOrderResponse() {
    }

    public FullOrderResponse(OrderResponse order, List<OrderDetailResponse> details) {
        this.order = order;
        this.details = details;
    }

    public OrderResponse getOrder() {
        return order;
    }

    public List<OrderDetailResponse> getDetails() {
        return details;
    }
}
