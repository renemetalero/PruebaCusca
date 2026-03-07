package com.shoppingcart.api.mapper;

import com.shoppingcart.api.dto.*;
import com.shoppingcart.api.entity.Client;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.OrderStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderMapper {

    public OrderEntity toEntity(Client client) {
        return OrderEntity.builder()
                .client(client)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public OrderResponse toResponse(OrderEntity order) {
        return new OrderResponse(order.getId(), order.getClient().getId(), order.getStatus(), order.getCreatedAt());
    }

    public FullOrderResponse toFullResponse(OrderEntity order, List<OrderDetailResponse> details) {
        return new FullOrderResponse(toResponse(order), details);
    }
}
