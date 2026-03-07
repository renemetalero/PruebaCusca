package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.OrderRegistrationDetailRequest;
import com.shoppingcart.api.dto.OrderRegistrationRequest;
import com.shoppingcart.api.dto.OrderRegistrationResponse;
import com.shoppingcart.api.entity.Client;
import com.shoppingcart.api.entity.OrderDetail;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.OrderStatus;
import com.shoppingcart.api.exception.BadRequestException;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

        Client client = Client.builder()
                .id(1L)
                .firstName("John")
                .lastName("-")
                .email("john@local")
                .build();

        OrderEntity order = OrderEntity.builder()
                .id(2L)
                .client(client)
                .status(OrderStatus.CREATED)
                .enabled(true)
                .build();

        when(clientRepository.findByEmail("john@local"))
                .thenReturn(Optional.of(client));

        when(orderRepository.save(any()))
                .thenReturn(order);

        OrderRegistrationResponse result = orderService.createOrder(request);

        assertEquals(2L, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenClientNotFound() {

        OrderRegistrationRequest request = new OrderRegistrationRequest();
        request.setCustomer("John");

        when(clientRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(NullPointerException.class,
                () -> orderService.createOrder(request));
    }

    @Test
    void shouldGetAllOrdersOnlyEnabled() {

        Client client = Client.builder()
                .id(1L)
                .firstName("Rene")
                .lastName("-")
                .email("rene@local")
                .build();

        OrderEntity order = OrderEntity.builder()
                .id(1L)
                .client(client)
                .status(OrderStatus.CREATED)
                .enabled(true)
                .build();

        when(orderRepository.findAllByEnabledTrue())
                .thenReturn(List.of(order));

        List<OrderRegistrationResponse> result = orderService.getAllOrders();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersExist() {

        when(orderRepository.findAllByEnabledTrue())
                .thenReturn(List.of());

        List<OrderRegistrationResponse> result = orderService.getAllOrders();

        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnOrderById() {

        Client client = Client.builder()
                .id(1L)
                .firstName("John")
                .lastName("-")
                .email("john@local")
                .build();

        OrderEntity order = OrderEntity.builder()
                .id(1L)
                .client(client)
                .status(OrderStatus.CREATED)
                .enabled(true)
                .build();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderRegistrationResponse response = orderService.getOrder(1L);

        assertEquals(1L, response.getId());
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> orderService.getOrder(1L));
    }

    @Test
    void shouldThrowWhenOrderNotFound() {
        when(orderRepository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.getOrderEntity(7L));
    }

    @Test
    void shouldUpdateOrderSuccessfully() {

        OrderRegistrationRequest request = new OrderRegistrationRequest();
        request.setId(1L);
        request.setCustomer("Rene");
        request.setOrderStatus("CREATED");

        Client client = Client.builder()
                .id(1L)
                .firstName("John")
                .lastName("-")
                .email("john@local")
                .build();

        OrderEntity order = OrderEntity.builder()
                .id(1L)
                .client(client)
                .status(OrderStatus.CREATED)
                .enabled(true)
                .build();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderDetailRepository.findByOrderId(1L))
                .thenReturn(List.of());

        when(orderRepository.save(any()))
                .thenReturn(order);

        OrderRegistrationResponse response =
                orderService.updateOrder(request);

        assertEquals(1L, response.getId());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {

        OrderRegistrationRequest request = new OrderRegistrationRequest();
        request.setCustomer("Rene");

        assertThrows(BadRequestException.class,
                () -> orderService.updateOrder(request));
    }

    @Test
    void shouldUpdateOrderWithoutUpdatingClient() {

        OrderRegistrationRequest request = new OrderRegistrationRequest();
        request.setId(1L);
        request.setOrderStatus("CREATED");

        Client client = Client.builder()
                .id(1L)
                .firstName("John")
                .lastName("-")
                .email("john@local")
                .build();

        OrderEntity order = OrderEntity.builder()
                .id(1L)
                .client(client)
                .status(OrderStatus.CREATED)
                .enabled(true)
                .build();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderDetailRepository.findByOrderId(1L))
                .thenReturn(List.of());

        when(orderRepository.save(any()))
                .thenReturn(order);

        OrderRegistrationResponse response =
                orderService.updateOrder(request);

        assertEquals(1L, response.getId());
    }

    @Test
    void shouldDisableOrder() {

        Client client = Client.builder()
                .id(1L)
                .firstName("John")
                .email("john@local")
                .lastName("-")
                .build();

        OrderEntity order = OrderEntity.builder()
                .id(1L)
                .client(client)
                .status(OrderStatus.CREATED)
                .enabled(true)
                .build();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        orderService.disableOrder(1L);

        assertEquals(false, order.getEnabled());
        assertEquals(OrderStatus.DISABLED, order.getStatus());
    }


    @Test
    void shouldSaveOrderDetails() {

        OrderRegistrationRequest request = new OrderRegistrationRequest();
        request.setId(1L);
        request.setOrderStatus("CREATED");

        OrderRegistrationDetailRequest d1 = new OrderRegistrationDetailRequest();
        d1.setIdProduct(10L);
        d1.setQuantity(2);
        d1.setPrice(BigDecimal.valueOf(20.0));

        OrderRegistrationDetailRequest d2 = new OrderRegistrationDetailRequest();
        d2.setIdProduct(20L);
        d2.setQuantity(1);
        d2.setPrice(BigDecimal.valueOf(15.0));

        request.setDetails(List.of(d1, d2));

        Client client = Client.builder()
                .id(1L)
                .firstName("John")
                .lastName("-")
                .email("john@local")
                .build();

        OrderEntity order = OrderEntity.builder()
                .id(1L)
                .client(client)
                .status(OrderStatus.CREATED)
                .enabled(true)
                .build();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderDetailRepository.findByOrderId(1L))
                .thenReturn(List.of());

        when(orderRepository.save(any()))
                .thenReturn(order);

        orderService.updateOrder(request);

        verify(orderDetailRepository, times(2)).save(any(OrderDetail.class));
    }

    @Test
    void shouldNotSaveDetailsWhenDetailsIsNull() {

        OrderRegistrationRequest request = new OrderRegistrationRequest();
        request.setId(1L);
        request.setOrderStatus("CREATED");
        request.setDetails(null);

        Client client = Client.builder()
                .id(1L)
                .firstName("John")
                .lastName("-")
                .email("john@local")
                .build();

        OrderEntity order = OrderEntity.builder()
                .id(1L)
                .client(client)
                .status(OrderStatus.CREATED)
                .enabled(true)
                .build();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderDetailRepository.findByOrderId(1L))
                .thenReturn(List.of());

        when(orderRepository.save(any()))
                .thenReturn(order);

        orderService.updateOrder(request);

        verify(orderDetailRepository, never()).save(any());
    }

}

