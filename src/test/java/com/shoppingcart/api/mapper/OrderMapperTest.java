package com.shoppingcart.api.mapper;

import com.shoppingcart.api.dto.OrderDtos;
import com.shoppingcart.api.entity.Client;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.OrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private final OrderMapper mapper = new OrderMapper();

    @Test
    void shouldCreateOrderEntityWithCreatedStatus() {
        Client client = Client.builder().id(10L).build();

        OrderEntity order = mapper.toEntity(client);

        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(10L, order.getClient().getId());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    void shouldMapOrderToResponse() {
        Client client = Client.builder().id(10L).build();
        OrderEntity order = OrderEntity.builder().id(99L).client(client).status(OrderStatus.CREATED).createdAt(LocalDateTime.now()).build();

        OrderDtos.OrderResponse response = mapper.toResponse(order);

        assertEquals(99L, response.id());
        assertEquals(10L, response.clientId());
    }

    @Test
    void shouldMapFullOrderResponse() {
        Client client = Client.builder().id(1L).build();
        OrderEntity order = OrderEntity.builder().id(7L).client(client).status(OrderStatus.CREATED).createdAt(LocalDateTime.now()).build();
        OrderDtos.OrderDetailResponse detail = new OrderDtos.OrderDetailResponse(1L, 7L, 1L, "P", 1, null, null);

        OrderDtos.FullOrderResponse response = mapper.toFullResponse(order, List.of(detail));

        assertEquals(7L, response.order().id());
        assertEquals(1, response.details().size());
    }
}
