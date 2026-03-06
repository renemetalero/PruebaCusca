package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.OrderDtos;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.OrderStatus;
import com.shoppingcart.api.entity.Payment;
import com.shoppingcart.api.entity.PaymentStatus;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private OrderService orderService;
    @Mock
    private OrderDetailService orderDetailService;
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void payShouldApprovePaymentWhenCardIsValid() {
        OrderEntity order = OrderEntity.builder().id(1L).status(OrderStatus.CREATED).build();
        when(orderService.getOrderEntity(1L)).thenReturn(order);
        when(orderDetailService.calculateOrderTotal(1L)).thenReturn(new BigDecimal("100.00"));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            payment.setId(5L);
            return payment;
        });

        OrderDtos.PaymentResponse response = paymentService.pay(new OrderDtos.PaymentRequest(1L, "12345678", "John"));

        assertEquals("APPROVED", response.status());
        assertEquals(5L, response.paymentId());
    }

    @Test
    void payShouldFailWhenOrderHasNoItems() {
        OrderEntity order = OrderEntity.builder().id(1L).status(OrderStatus.CREATED).build();
        when(orderService.getOrderEntity(1L)).thenReturn(order);
        when(orderDetailService.calculateOrderTotal(1L)).thenReturn(BigDecimal.ZERO);

        assertThrows(BadRequestException.class,
                () -> paymentService.pay(new OrderDtos.PaymentRequest(1L, "12345678", "John")));
    }
}
