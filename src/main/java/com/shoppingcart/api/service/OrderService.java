package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.OrderDtos;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.exception.NotFoundException;
import com.shoppingcart.api.mapper.OrderMapper;
import com.shoppingcart.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final OrderMapper orderMapper;

    public OrderDtos.OrderResponse createOrder(OrderDtos.CreateOrderRequest request) {
        if (request.clientId() == null) {
            throw new BadRequestException("clientId is required");
        }

        OrderEntity order = orderMapper.toEntity(clientService.findById(request.clientId()));

        return orderMapper.toResponse(orderRepository.save(order));
    }

    public OrderEntity getOrderEntity(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public OrderDtos.OrderResponse getOrder(Long id) {
        return orderMapper.toResponse(getOrderEntity(id));
    }

    public void save(OrderEntity order) {
        orderRepository.save(order);
    }
}
