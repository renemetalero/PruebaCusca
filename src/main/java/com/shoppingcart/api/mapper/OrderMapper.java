package com.shoppingcart.api.mapper;

import com.shoppingcart.api.dto.OrderDtos;
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

    public OrderDtos.OrderResponse toResponse(OrderEntity order) {
        return new OrderDtos.OrderResponse(order.getId(), order.getClient().getId(), order.getStatus(), order.getCreatedAt());
    }

    public OrderDtos.FullOrderResponse toFullResponse(OrderEntity order, List<OrderDtos.OrderDetailResponse> details) {
        return new OrderDtos.FullOrderResponse(toResponse(order), details);
    }
}
