package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.OrderRegistrationRequest;
import com.shoppingcart.api.dto.OrderRegistrationResponse;
import com.shoppingcart.api.entity.Client;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.exception.NotFoundException;
import com.shoppingcart.api.repository.ClientRepository;
import com.shoppingcart.api.repository.OrderDetailRepository;
import com.shoppingcart.api.repository.OrderRepository;
import com.shoppingcart.api.repository.PaymentRepository;
import com.shoppingcart.api.serviceImpl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldCreateOrder() {
        OrderRegistrationRequest request = new OrderRegistrationRequest();
        request.setCustomer("John");

        Client client = Client.builder().id(1L).firstName("John").email("john@local").lastName("-").build();
        OrderEntity order = OrderEntity.builder().id(2L).client(client).build();

        when(clientRepository.findByEmail("john@local")).thenReturn(Optional.of(client));
        when(orderRepository.save(org.mockito.ArgumentMatchers.any())).thenReturn(order);

        OrderRegistrationResponse result = orderService.createOrder(request);

        assertEquals(2L, result.getId());
    }

    @Test
    void shouldThrowWhenOrderNotFound() {
        when(orderRepository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.getOrderEntity(7L));
    }
}
