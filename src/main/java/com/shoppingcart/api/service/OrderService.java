package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.*;
import com.shoppingcart.api.entity.Client;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.exception.NotFoundException;
import com.shoppingcart.api.mapper.OrderMapper;
import com.shoppingcart.api.repository.ClientRepository;
import com.shoppingcart.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final OrderMapper orderMapper;

    public OrderResponse createOrder(CreateOrderRequest request) {
        Client client = request.getClientId() != null
                ? clientService.findById(request.getClientId())
                : getOrCreateDefaultClient();

        OrderEntity order = orderMapper.toEntity(client);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    public OrderEntity getOrderEntity(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public OrderResponse getOrder(Long id) {
        return orderMapper.toResponse(getOrderEntity(id));
    }

    public void save(OrderEntity order) {
        orderRepository.save(order);
    }

    private Client getOrCreateDefaultClient() {
        return clientRepository.findByEmail("compat.customer@local").orElseGet(() ->
                clientRepository.save(Client.builder()
                        .firstName("Compat")
                        .lastName("Customer")
                        .email("compat.customer@local")
                        .build())
        );
    }
}
