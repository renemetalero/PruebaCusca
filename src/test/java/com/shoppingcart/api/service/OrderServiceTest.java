package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.OrderDtos;
import com.shoppingcart.api.entity.Client;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.exception.NotFoundException;
import com.shoppingcart.api.mapper.OrderMapper;
import com.shoppingcart.api.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldThrowWhenClientIdIsMissing() {
        assertThrows(BadRequestException.class, () -> orderService.createOrder(new OrderDtos.CreateOrderRequest(null)));
    }

    @Test
    void shouldCreateOrder() {
        Client client = Client.builder().id(1L).build();
        OrderEntity order = OrderEntity.builder().id(2L).client(client).build();
        OrderDtos.OrderResponse response = new OrderDtos.OrderResponse(2L, 1L, null, null);

        when(clientService.findById(1L)).thenReturn(client);
        when(orderMapper.toEntity(client)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(response);

        OrderDtos.OrderResponse result = orderService.createOrder(new OrderDtos.CreateOrderRequest(1L));

        assertEquals(2L, result.id());
    }

    @Test
    void shouldThrowWhenOrderNotFound() {
        when(orderRepository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.getOrderEntity(7L));
    }
}
